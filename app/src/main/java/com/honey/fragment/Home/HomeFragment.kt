package com.honey.fragment.Home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.honey.R
import com.honey.activity.HomeFilter.HomeFilterActivity
import com.honey.activity.Search.SearchActivity
import com.honey.adapter.HomeBannerAdapter
import com.honey.adapter.HomeOptionAdapter
import com.honey.adapter.CommonHomeAdapter
import com.honey.base.BaseFragment
import com.honey.model.request.CommonModel
import com.honey.model.response.success.CommonShopsItemModel
import com.honey.model.response.success.ResponseBean
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.PERMISSION
import com.honey.utils.CommonUtils.Companion.PERMISSION_DIALOG_REQ
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.TAG
import kotlinx.android.synthetic.main.activity_walk_through.viewPager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.ivFilter
import kotlinx.android.synthetic.main.fragment_home.tabLayout
import me.kungfucat.viewpagertransformers.DepthPageTransformer
import java.util.*

class HomeFragment : BaseFragment(), View.OnClickListener, HomeOptionAdapter.onOptionClickListner, CommonHomeAdapter.setOnShopClickListner, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnSuccessListener<Location> {
    private lateinit var homeViewModel: HomeViewModel
    private var homeData: ResponseBean?=null
    private var homeDataList :List<CommonShopsItemModel>?=null
    var pos:Int?=0
    val FILTER_RESULT_REQ:Int=10
    private var googleApiClient: GoogleApiClient? = null
    private var locationRequest: LocationRequest? = null
    private var isLocServiceStarted = false
    private var locationCallback: LocationCallback? = null
    var lat: Double? = null
    var lng: Double? = null
    private var mLastLocation: Location? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var currentPos=0
    val handler=Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(checkPermissions()) { startLocationFunctioning()}
    }


    private fun checkPermissions(): Boolean {
        var ret = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ret = false
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION)
            }
        }
        return ret
    }

    fun startLocationFunctioning() {
        if (!CommonUtils.isOnline(requireActivity())) {
            Toast.makeText(requireActivity(), "Internet not available.", Toast.LENGTH_SHORT).show()
        } else {
            if (CommonUtils.isGPlayServicesOK(requireActivity())) {
                buildGoogleApiClient()
            }
        }
    }

    private fun buildGoogleApiClient() {
        googleApiClient = GoogleApiClient.Builder(requireActivity())
            .addApi(LocationServices.API)
            .enableAutoManage(requireActivity(), 0, this)
            .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
        googleApiClient!!.connect()
        createLocationRequest()
    }

    override fun onPause() {
        super.onPause()
        googleApiClient!!.stopAutoManage(requireActivity())
        googleApiClient!!.disconnect();
    }

    override fun onStop() {
        super.onStop()
        if (googleApiClient!!.isConnected()) {
            googleApiClient!!.stopAutoManage(requireActivity())
            googleApiClient!!.disconnect()
        }
    }
    fun createLocationRequest() {
        locationRequest = LocationRequest.create()
        locationRequest!!.setInterval(2000)
        locationRequest!!.setFastestInterval(10 * 1000.toLong())
        locationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }


    @SuppressLint("MissingPermission")
    fun loadCurrentLoc() {
        try {
            mFusedLocationClient!!.lastLocation.addOnSuccessListener(this)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    Log.e(TAG(this), "" + locationResult)
                    for (location in locationResult.locations) {
                        if (location != null) {
                            if (!isLocServiceStarted) {
                                locationCallBack(location)
                            }
                        }
                    }
                }
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
        initControl()
        myObserver()
    }

    fun setUpLocationSettingsTaskStuff() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest!!)
        builder.setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { loadCurrentLoc() }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    Log.e("requested", "yes")
                    e.startResolutionForResult(requireActivity(), PERMISSION_DIALOG_REQ)
                } catch (sendEx: IntentSender.SendIntentException) {
                    sendEx.printStackTrace()
                }
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onConnected(p0: Bundle?) {
        setUpLocationSettingsTaskStuff()
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onSuccess(location: Location?) {
        if (location != null)  {locationCallBack(location) }
        else { isLocServiceStarted=false }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

                if (permissionDenied) CommonUtils.showSnackBar(requireActivity(), "Please Allow permission for the security purpose")
                else startLocationFunctioning()
            }
        }
    }

    override fun init() {

        // Options RecycleView
        var options = arrayListOf("ALL", "LATEST", "POPULAR", "OFFERS", "DISTANCE")
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation=LinearLayoutManager.HORIZONTAL
        rvOptions.layoutManager=layoutManager
        rvOptions.adapter=HomeOptionAdapter(requireContext(), "Home", options, this)
        rvOptions.scheduleLayoutAnimation()
        Log.e(TAG(this),"Latitude : "+lat+" Longitude : "+lng)
        prefs.latitude=""+lat
        prefs.longitude=""+lng
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        homeViewModel.homeProductApi(requireActivity(), prefs.jwtToken!!, lat!!, lng!!)
    }

    override fun initControl() {
        edFind.setOnClickListener(this)
        ivFilter.setOnClickListener(this)
    }

    override fun myObserver(){

        homeViewModel.response.observe(requireActivity(), Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it.response)
            else if (it.status!!.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(activity, it.message)
        })

        homeViewModel.onFavResponse.observe(requireActivity(), Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkFavData(it)
            else if (it.status!!.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(activity, it.message)
        })

        homeViewModel.error.observe(requireActivity(), Observer {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        })
    }

    private fun checkFavData(response: CommonModel?) {
       if(response!!.message.equals("Store added to wishlist successfully")) homeDataList!!.get(pos!!).favourite="yes"
       else if(response.message.equals("Store removed from wishlist successfully")) homeDataList!!.get(
           pos!!
       ).favourite="no"
        rvFeaturedShops.adapter!!.notifyItemChanged(pos!!)
    }
    private fun setDataToUi(data: ResponseBean?) {
        homeData=data

        // Banner View Pager
        Collections.shuffle(data!!.banner!!)
        viewPager.adapter=HomeBannerAdapter(requireContext(), data!!.banner!!)
        viewPager.setPageTransformer(true, DepthPageTransformer())
        tabLayout.setupWithViewPager(viewPager)

        val runnable=Runnable {
            if (currentPos == data.banner!!.size!! - 1) currentPos = 0
            else currentPos++
            if(viewPager!=null) {
                viewPager.setCurrentItem(currentPos, true)
            }
        }

        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 500, 3000)

        // Shops
        homeDataList=data!!.allstores
        Collections.shuffle(homeDataList)
        var label=if(homeDataList!!.size>0) "ALL SHOPS" else "ALL SHOP"
        tvLabel.text=label
        val homeAdapter=CommonHomeAdapter(requireContext(), homeDataList!!,"ALL",this)
        rvFeaturedShops.adapter=homeAdapter
        homeAdapter.notifyDataSetChanged()
        rvFeaturedShops.scheduleLayoutAnimation()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.edFind -> { startActivity(requireActivity(), SearchActivity::class.java) }
            R.id.ivFilter -> { startActivity(requireActivity(), HomeFilterActivity::class.java) }
        }
    }

    fun startActivity(context: Context, className: Class<out Any?>?) {
            val intent = Intent(context, className)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)
    }

    override fun onSelectedOtion(option: String) {
        when(option.toLowerCase())
        {
            "all" -> { homeDataList = homeData!!.allstores }
            "latest" -> { homeDataList = homeData!!.latest }
            "popular" -> { homeDataList = homeData!!.popular }
            "offers" -> { homeDataList = homeData!!.offers }
            "distance" -> { homeDataList = homeData!!.distance }
        }
        val label=if(option.equals("OFFERS",ignoreCase = true)) if(homeDataList!!.size>0) "$option " else "OFFER" else if(option.equals("DISTANCE",ignoreCase = true)) if(homeDataList!!.size>0) "NEAR BY SHOPS" else "NEAR BY SHOP"  else if(homeDataList!!.size>0) "$option SHOPS" else "$option SHOP"
        tvLabel.text=label
        Collections.shuffle(homeDataList)
        Log.e(TAG(this), "" + homeDataList!!.size)

        if(homeDataList!!.size>0)
        {
            lottieAnim.visibility=View.GONE
            rvFeaturedShops.visibility=View.VISIBLE
        }else{
            lottieAnim.visibility=View.VISIBLE
            rvFeaturedShops.visibility=View.GONE
        }

        rvFeaturedShops.adapter=CommonHomeAdapter(requireContext(), homeDataList!!,option, this)
        rvFeaturedShops.scheduleLayoutAnimation()
    }

    override fun onFav(pos: Int, storeId: String) {
        this.pos=pos
        homeViewModel.addToWishApi(prefs.jwtToken!!, storeId, "" + ParamEnum.STORE.theValue())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG(this), "Request Code : " + requestCode + "\n Result Code : " + resultCode)

        when(requestCode)
        {
            FILTER_RESULT_REQ -> {
                when (resultCode) {
                    Activity.RESULT_OK -> {

                    }
                    Activity.RESULT_CANCELED -> {
                    }
                }
            }


            PERMISSION_DIALOG_REQ -> {
                if (resultCode == Activity.RESULT_OK) loadCurrentLoc()
                else if (resultCode == Activity.RESULT_CANCELED) Log.e(
                    "error",
                    "Please allow permissions"
                )
            }
    }

}}

