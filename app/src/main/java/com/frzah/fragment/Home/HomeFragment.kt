package com.frzah.fragment.Home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.activity.HomeFilter.HomeFilterActivity
import com.frzah.activity.Search.SearchActivity
import com.frzah.adapter.CommonHomeAdapter
import com.frzah.adapter.HomeBannerAdapter
import com.frzah.adapter.HomeOptionAdapter
import com.frzah.base.BaseFragment
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.CommonShopsItemModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.model.response.success.TypeModel
import com.frzah.utils.CommonUtils.Companion.DELAY_MS
import com.frzah.utils.CommonUtils.Companion.PERIOD_MS
import com.frzah.utils.CommonUtils.Companion.PERMISSION
import com.frzah.utils.CommonUtils.Companion.PERMISSION_DIALOG_REQ
import com.frzah.utils.CommonUtils.Companion.isGPlayServicesOK
import com.frzah.utils.CommonUtils.Companion.isOnline
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.showSnackBarGreen
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.frzah.utils.ViewExtension.TAG
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_shop_details.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.rvOptions
import me.kungfucat.viewpagertransformers.DepthPageTransformer
import java.util.*

class HomeFragment(var tvBadges: TextView?) : BaseFragment(), View.OnClickListener, HomeOptionAdapter.onOptionClickListner, CommonHomeAdapter.setOnShopClickListner, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, OnSuccessListener<Location> {
    constructor():this(null)
    private lateinit var homeViewModel: HomeViewModel
    private var homeData: ResponseBean?=null
    private var homeDataList :List<CommonShopsItemModel>?=null
    var pos:Int?=0
    val FILTER_RESULT_REQ:Int=10
    private  var googleApiClient: GoogleApiClient?=null
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    var lat: Double? = null
    var lng: Double? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var currentPos=0
    val handler= Handler()
    var isLocation:Boolean=false


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(checkPermission()) startLocationFunctioning()
    }

    fun checkPermission() : Boolean{
        var ret=true
        if (ActivityCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ret=false
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), PERMISSION)
            }
        }

        return ret
    }


    fun startLocationFunctioning() {
        if (!isOnline(requireActivity())) { Toast.makeText(requireActivity(), getString(R.string.internet_not_available), Toast.LENGTH_SHORT).show()
        } else {
            if (isGPlayServicesOK(requireActivity())) {
                buildGoogleApiClient()
            }
        }
    }

    private fun buildGoogleApiClient() {
        try {
            googleApiClient = GoogleApiClient.Builder(requireActivity())
                .addApi(LocationServices.API)
                .enableAutoManage(requireActivity(), 0, this)
                .addApi(com.google.android.gms.location.places.Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()
            googleApiClient?.connect()
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

    }

    override fun onPause() {
        super.onPause()
        googleApiClient?.stopAutoManage(requireActivity())
        googleApiClient?.disconnect()
    }

    override fun onStop() {
        super.onStop()
        if(googleApiClient!=null) {
            if (googleApiClient!!.isConnected()) {
                googleApiClient!!.stopAutoManage(requireActivity())
                googleApiClient!!.disconnect()
            }
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
            locationCallback=object: LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        if (location != null) {
                            mFusedLocationClient?.removeLocationUpdates(locationCallback)
                            locationCallBack(location)
                        }
                    }
                }
            }
             mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
             mFusedLocationClient!!.lastLocation.addOnSuccessListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun locationCallBack(location: Location?) {
        mFusedLocationClient?.removeLocationUpdates(locationCallback)
        isLocation = true
        lat = location!!.latitude
        lng = location.longitude
        init()
        initControl()
        myObserver()
    }

    fun setUpLocationSettingsTaskStuff() {
        val builder = LocationSettingsRequest.Builder().addLocationRequest(createLocationRequest()!!)
        builder.setAlwaysShow(true)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener { loadCurrentLoc() }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try { e.startResolutionForResult(requireActivity(), PERMISSION_DIALOG_REQ)
                }
                catch (sendEx: IntentSender.SendIntentException) {
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
                for (i in 0 until grantResults.size) {
                    if (grantResults.get(i) == PackageManager.PERMISSION_DENIED) {
                        permissionDenied = true
                        break
                    }
                }
                if (permissionDenied) showSnackBar(requireActivity(), getString(R.string.please_allow_permission_for_security))
                else startLocationFunctioning()
            }
        }
    }

    override fun init() {
        val height: Int = Resources.getSystem().displayMetrics.heightPixels
        val param  = rvFeaturedShops.layoutParams
        param.height=height- rvOptions.height - requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).height - requireActivity().findViewById<ConstraintLayout>(R.id.clMainToolbar).height -280
        rvFeaturedShops.layoutParams=param

        // Options RecycleView
//        val options = arrayListOf("ALL", "LATEST", "POPULAR", "OFFERS", "DISTANCE")
        val options = arrayListOf<TypeModel>(TypeModel(getString(R.string.all),getString(R.string.all)),
                                             TypeModel(getString(R.string.latest),getString(R.string.latest)),
                                             TypeModel(getString(R.string.popular_caps),getString(R.string.popular_caps)),
                                             TypeModel(getString(R.string.distance_caps),getString(R.string.distance_caps)))
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation=LinearLayoutManager.HORIZONTAL
        rvOptions.layoutManager=layoutManager
        rvOptions.adapter=HomeOptionAdapter(requireContext(), "Home", options, this)
        rvOptions.scheduleLayoutAnimation()
        Log.e(TAG(this), "Latitude : $lat Longitude : $lng")
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
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(activity, it.message)
        })

        homeViewModel.notificationResponse.observe(requireActivity(), Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkNotificationBadge(it.response)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(activity, it.message)
        })

        homeViewModel.onFavResponse.observe(requireActivity(), Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkFavData(it)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(activity, it.message)
        })

        homeViewModel.error.observe(requireActivity(), Observer { ErrorUtil.handlerGeneralError(requireActivity(), it) })
    }

    private fun checkNotificationBadge(response: ResponseBean?) {
        if(response!!.count.equals("0")) tvBadges?.visibility=View.GONE
        else tvBadges?.visibility=View.VISIBLE
        tvBadges?.text=response!!.count
    }

    private fun checkFavData(response: CommonModel?) {
       if(response!!.message.equals(getString(R.string.store_added_to_wishlist))) homeDataList!!.get(
           pos!!).favourite="yes"
       else if(response.message.equals(getString(R.string.store_removed_from_wishlist))) homeDataList!!.get(
           pos!!).favourite="no"
        rvFeaturedShops.adapter!!.notifyItemChanged(pos!!)
    }
    private fun setDataToUi(data: ResponseBean?) {
        homeData=data

        // Banner View Pager
        Collections.shuffle(data!!.banner!!)
        viewPager.adapter= HomeBannerAdapter(requireContext(), data!!.banner!!)
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
        }, DELAY_MS, PERIOD_MS)



        // Shops
        homeDataList=data!!.allstores
        Collections.shuffle(homeDataList)
        val label=if(homeDataList!!.size>0) getString(R.string.all_shops) else getString(R.string.all_shop)
        tvLabel.text=label
        val homeAdapter=CommonHomeAdapter(requireContext(), homeDataList!!, getString(R.string.all), this)
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

    override fun onSelectedOtion(option: String,position: Int) {
        when(position)
        {
            0 -> { homeDataList = homeData!!.allstores }
            1 -> { homeDataList = homeData!!.latest }
            2 -> { homeDataList = homeData!!.popular }
//            3 -> { homeDataList = homeData!!.offers }
            3 -> { homeDataList = homeData!!.distance }
        }
        Log.e("option",option)

        if(prefs.selectedLanguage.equals("en")) option.toLowerCase()
        val label=if(option.equals(getString(R.string.offers), ignoreCase = true)) if(homeDataList!!.size>0) "$option " else getString(
            R.string.offer) else if(option.equals(getString(R.string.distance_caps), ignoreCase = true)) if(homeDataList!!.size>0) getString(R.string.near_by_shops) else getString(
            R.string.near_by_shop)  else if(homeDataList!!.size>0) "$option "+getString(R.string.shops_cap) else "$option "+getString(
            R.string.shop_cap)
        tvLabel.text=label
        Collections.shuffle(homeDataList)
        Log.e(TAG(this), "" + homeDataList!!.size)
        Log.e("option",option)


        if(homeDataList!!.size>0)
        {
            lottieAnim.visibility=View.GONE
            rvFeaturedShops.visibility=View.VISIBLE
        }else{
            lottieAnim.visibility=View.VISIBLE
            rvFeaturedShops.visibility=View.GONE
        }

        rvFeaturedShops.adapter=CommonHomeAdapter(requireContext(), homeDataList!!, option, this)
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
                if (resultCode == Activity.RESULT_OK) {
                    loadCurrentLoc()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    showSnackBarGreen(requireActivity(), getString(R.string.please_turn_gps))
                    setUpLocationSettingsTaskStuff()
                }
            }
    }

}

    @SuppressLint("MissingPermission")
    override fun onSuccess(p0: Location?) {
        if(p0!=null) locationCallBack(p0)
        else mFusedLocationClient!!.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }
}

