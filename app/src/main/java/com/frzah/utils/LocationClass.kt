package com.frzah.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils.Companion.PERMISSION
import com.frzah.utils.CommonUtils.Companion.PERMISSION_DIALOG_REQ
import com.frzah.utils.CommonUtils.Companion.isGPlayServicesOK
import com.frzah.utils.CommonUtils.Companion.isOnline
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.showSnackBarGreen
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class LocationClass(var context: Context) : BaseActivity(), GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    val fragmentActivity = (context as Activity) as FragmentActivity
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    var lat: Double? = null
    var lng: Double? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun init() {
        if(checkPermissions()) { startLocationFunctioning()}
    }

    override fun initControl() {
        locationCallback=object: LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
        Log.e(ViewExtension.TAG(this), "locationResult::" + locationResult)
        for (location in locationResult.locations) {
        if (location != null) {
            lat = location.latitude
            lng = location.longitude
            mFusedLocationClient!!.removeLocationUpdates(locationCallback)
            val intent=Intent()
            intent.putExtra(ParamEnum.LATITUDE.theValue() as String, lat!!)
            intent.putExtra(ParamEnum.LONGITUDE.theValue() as String, lng!!)
            setResult(RESULT_OK,intent)
            finish()
        }
        }
        }
        }
    }

    override fun myObserver() {
    }

    private fun checkPermissions(): Boolean {
        var ret = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context as Activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(context as Activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ret = false
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION)
            }
        }
        return ret
    }


    fun startLocationFunctioning() {
        if (!isOnline(context = context)) {
            Toast.makeText(context as Activity, "Internet not available.", Toast.LENGTH_SHORT).show()
        } else {
            if (isGPlayServicesOK(context as Activity)) {
                buildGoogleApiClient()
            }
        }
    }

    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API)
            .enableAutoManage(fragmentActivity, 0, this)
            .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        googleApiClient!!.connect()
    }


    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        setUpLocationSettingsTaskStuff()
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    fun setUpLocationSettingsTaskStuff() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest()!!)
        builder.setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(context as Activity)
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { loadCurrentLoc() }
        task.addOnFailureListener { e -> if (e is ResolvableApiException) {
                try { e.startResolutionForResult(context as Activity,PERMISSION_DIALOG_REQ) }
                catch (sendEx: IntentSender.SendIntentException) { sendEx.printStackTrace() } }
        }
    }

    fun  createLocationRequest() : LocationRequest? {
        locationRequest = LocationRequest.create()
        locationRequest!!.setInterval(2000)
        locationRequest!!.setFastestInterval(10 * 1000.toLong())
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        return locationRequest
    }


    @SuppressLint("MissingPermission")
    fun loadCurrentLoc() {
        try {
            initControl()
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context as Activity)
            mFusedLocationClient!!.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        }
        catch (e: Exception) { e.printStackTrace() }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(ViewExtension.TAG(this), "Request Code : " + requestCode + "\n Result Code : " + resultCode)
        when(requestCode)
        {
            PERMISSION_DIALOG_REQ -> {
            if (resultCode == Activity.RESULT_OK) { loadCurrentLoc() }
            else if (resultCode == Activity.RESULT_CANCELED) {
                showSnackBarGreen(context as Activity,"Please turn on gps for the security purpose")
                setUpLocationSettingsTaskStuff()
            }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            PERMISSION -> {
                var permissionDenied = false
                for (i in 0..(grantResults.size - 1)) {
                    if (grantResults.get(i) == PackageManager.PERMISSION_DENIED) {
                        permissionDenied = true
                        break
                    }
                }
                if (permissionDenied) {
                    showSnackBar(context as Activity, "Please Allow permission for the security purpose")
                }
                else startLocationFunctioning()
            }
        }
    }
}