package com.frzah.activity.SelectLocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.*
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.activity.AddNewAddress.AddNewAddressActivity
import com.frzah.activity.SearchLocation.SearchLocationActivity
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils.Companion.GETTING_ADDRESS
import com.frzah.utils.CommonUtils.Companion.HIDE_INFO_WINDOW
import com.frzah.utils.CommonUtils.Companion.NOT_SERVE_THIS_AREA
import com.frzah.utils.CommonUtils.Companion.PERMISSION
import com.frzah.utils.CommonUtils.Companion.PERMISSION_DIALOG_REQ
import com.frzah.utils.CommonUtils.Companion.PLACE_REQ_CODE
import com.frzah.utils.CommonUtils.Companion.isGPlayServicesOK
import com.frzah.utils.CommonUtils.Companion.isOnline
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.showSnackBarGreen
import com.frzah.utils.ParamEnum
import com.frzah.utils.ViewExtension
import com.frzah.utils.ViewExtension.setLocale
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import kotlinx.android.synthetic.main.activity_select_location.*
import java.util.*
import kotlin.collections.ArrayList

class SelectLocationActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener, View.OnClickListener, GoogleMap.OnCameraIdleListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnSuccessListener<Location> {
    private var handler: Handler?=null
    private lateinit var selectLocationViewModel : SelectLocationViewModel
    private var mMap: GoogleMap? = null
    private var marker : Marker?=null
    private var markerPoints=ArrayList<LatLng>()
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var isLocServiceStarted = false
    private var locationCallback: LocationCallback? = null
    var lat: Double? = null
    var lng: Double? = null
    private var mLastLocation: Location? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)
        setContentView(R.layout.activity_select_location)
        startLocationFunctioning()
    }

    override fun init() {
        selectLocationViewModel = ViewModelProviders.of(this).get(SelectLocationViewModel::class.java)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun initControl() {
        btnConfirm.setOnClickListener(this)
        edFind.setOnClickListener(this)
        mMap!!.setOnCameraMoveListener(this)
        mMap!!.setOnCameraIdleListener(this)
    }

    override fun myObserver() {
        selectLocationViewModel.currentAddressSuccess.observe(this, Observer { tvAddress.setText(""+it) })
        selectLocationViewModel.error.observe(this, Observer { showSnackBar(this,it.message) })
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        initControl()
        val sydney = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)
        val location_marker = ContextCompat.getDrawable(this,R.drawable.bitmap_reached)
        mMap?.apply {
            marker=addMarker(MarkerOptions().position(sydney).title("Your Location").icon(BitmapDescriptorFactory.fromBitmap(location_marker!!.toBitmap(location_marker.intrinsicWidth, location_marker.intrinsicHeight, null))))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,21.0f))
        }

        handler = object : Handler(Looper.myLooper()!!) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                if (msg.what == GETTING_ADDRESS) {
                    marker!!.setTitle(getString(R.string.getting_address))
                    marker!!.showInfoWindow()
                } else if (msg.what == NOT_SERVE_THIS_AREA) {
                    marker!!.setTitle(getString(R.string.sorry_not_server_here))
                    marker!!.showInfoWindow()
                } else if (msg.what == HIDE_INFO_WINDOW) {
                    marker!!.hideInfoWindow()
                }
            }
        }

        markerPoints.add(sydney)
        selectLocationViewModel.getCurrentAddres(this,mLastLocation!!.latitude, mLastLocation!!.longitude,handler!!)
    }

    override fun onCameraMove() {
        handler!!.sendEmptyMessage(HIDE_INFO_WINDOW)
        marker!!.setPosition(mMap!!.cameraPosition.target)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnConfirm ->{
                if(!tvAddress.text.toString().equals("")) fireIntent()
                else Toast.makeText(this,getString(R.string.didnot_get_address_yet),Toast.LENGTH_LONG).show()
            }

            R.id.edFind ->{
                Places.initialize(this, resources.getString(R.string.google_map_api_key))
                val fields1 = Arrays.asList(Place.Field.LAT_LNG)
                val intent1 = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields1).build(this)
                startActivityForResult(intent1, PLACE_REQ_CODE)
            }
        }
    }

    private fun fireIntent() {
        var address =""; var city="" ; var state="" ; var country="" ; var postalCode=""; var knownName=""
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(mMap!!.cameraPosition.target.latitude, mMap!!.cameraPosition.target.longitude, 1)
        if(addresses.size>0) {
            try {
                address = addresses[0].getAddressLine(0)!!
                city = addresses[0].locality!!
                state = addresses[0].adminArea!!
                country = addresses[0].countryName!!
                postalCode = addresses[0].postalCode!!
                knownName = addresses[0].featureName!!
            }catch (e:Exception){
                e.printStackTrace()
            }

            if(getIntent().getStringExtra("cameFrom").equals(SearchLocationActivity::class.simpleName))
            {
                val intent =Intent(this,AddNewAddressActivity::class.java)
                intent.putExtra("cameFrom",SearchLocationActivity::class.simpleName)
                intent.putExtra(ParamEnum.ADDRESS.theValue().toString(),address)
                intent.putExtra(ParamEnum.CITY.theValue().toString(),city)
                intent.putExtra(ParamEnum.STATE.theValue().toString(),state)
                intent.putExtra(ParamEnum.COUNTRY.theValue().toString(),country)
                intent.putExtra(ParamEnum.PIN_CODE.theValue().toString(),postalCode)
                intent.putExtra(ParamEnum.LOCALITY.theValue().toString(),knownName)
                intent.putExtra(ParamEnum.LATITUDE.theValue().toString(),mMap!!.cameraPosition.target.latitude)
                intent.putExtra(ParamEnum.LONGITUDE.theValue().toString(),mMap!!.cameraPosition.target.longitude)
                intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            else {
                val intent=Intent()
                intent.putExtra(ParamEnum.ADDRESS.theValue().toString(),address)
                intent.putExtra("cameFrom",AddNewAddressActivity::class.simpleName)
                intent.putExtra(ParamEnum.CITY.theValue().toString(),city)
                intent.putExtra(ParamEnum.STATE.theValue().toString(),state)
                intent.putExtra(ParamEnum.COUNTRY.theValue().toString(),country)
                intent.putExtra(ParamEnum.PIN_CODE.theValue().toString(),postalCode)
                intent.putExtra(ParamEnum.LOCALITY.theValue().toString(),knownName)
                intent.putExtra(ParamEnum.LATITUDE.theValue().toString(),mMap!!.cameraPosition.target.latitude)
                intent.putExtra(ParamEnum.LONGITUDE.theValue().toString(),mMap!!.cameraPosition.target.longitude)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

        }else{
            Toast.makeText(this,getString(R.string.didnot_get_address_yet),Toast.LENGTH_LONG).show()
        }
    }

    override fun onCameraIdle() {
        handler!!.sendEmptyMessage(GETTING_ADDRESS)
        selectLocationViewModel.getCurrentAddres(this,mMap!!.cameraPosition.target.latitude,mMap!!.cameraPosition.target.longitude,handler!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {

            PLACE_REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    markerPoints!!.clear()
                    markerPoints.add(place.latLng!!)
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 21.0f))
                }
            }

            PERMISSION_DIALOG_REQ -> {
                if (resultCode == Activity.RESULT_OK) { loadCurrentLoc() }
                else if (resultCode == Activity.RESULT_CANCELED) {
                    showSnackBarGreen(this,getString(R.string.please_turn_gps))
                    setUpLocationSettingsTaskStuff()
                }
            }
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            PERMISSION ->{
                var permissionDenied=false
                for(i in 0..(grantResults.size-1))
                {
                    if(grantResults.get(i)==PackageManager.PERMISSION_DENIED)
                    {
                        permissionDenied=true
                        break
                    }

                }

                if(permissionDenied) showSnackBar(this,getString(R.string.please_allow_permission_for_security))
                else startLocationFunctioning()
            }
        }
    }


    fun startLocationFunctioning() {
        if (!isOnline(this)) {
            Toast.makeText(this, getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show()
        } else {
            if (isGPlayServicesOK(this)) {
                buildGoogleApiClient()
            }
        }
    }

    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .enableAutoManage(this, 1, this)
            .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
            googleApiClient!!.connect()
        createLocationRequest()
    }

    fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest!!.setInterval(2000)
        locationRequest!!.setFastestInterval(10 * 1000.toLong())
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }


    fun loadCurrentLoc() {
        try {
            locationCallback=object: LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Log.e(ViewExtension.TAG(this), "" + locationResult)
                    for (location in locationResult.locations) {
                        if (location != null) {
                            locationCallBack(location)
                            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION)
                }
            }
            else {
                mFusedLocationClient!!.lastLocation.addOnSuccessListener(this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun locationCallBack(location: Location?) {
        mLastLocation = location
        lat = mLastLocation!!.latitude
        lng = mLastLocation!!.longitude
        isLocServiceStarted = true
        init()
        myObserver()
    }

    fun setUpLocationSettingsTaskStuff() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            loadCurrentLoc()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(this, PERMISSION_DIALOG_REQ)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Log.e("onFailed","onConnectionFailed")
    }

    override fun onConnected(p0: Bundle?) {
        Log.e("onConnected","onConnected")
        setUpLocationSettingsTaskStuff()
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.e("onSuspended","onConnectionSuspended")
    }

    @SuppressLint("MissingPermission")
    override fun onSuccess(p0: Location?) {
        if(p0!=null) locationCallBack(p0)
        else mFusedLocationClient!!.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}