package com.honey.activity.Main

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.honey.R
import com.honey.activity.DeliveryAddress.DeliveryAddressActivity
import com.honey.activity.MyProfile.MyProfileActivity
import com.honey.activity.SearchLocation.SearchLocationActivity
import com.honey.base.BaseActivity
import com.honey.fragment.Bag.BagFragment
import com.honey.fragment.Favorite.FavoriteFragment
import com.honey.fragment.Home.HomeFragment
import com.honey.fragment.Notification.NotificationFragment
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*
import androidx.lifecycle.Observer
import com.honey.activity.ContactUs.ContactUsActivity
import com.honey.activity.FAQs.FAQsActivity
import com.honey.activity.Login.LoginActivity
import com.honey.activity.Order.OrderActivity
import com.honey.activity.Payment.PaymentActivity
import com.honey.activity.SelectLanguage.SelectLanguageActivity
import com.honey.activity.Webview.WebviewActivity
import com.honey.firebase.MyFirebaseMessageService
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils.Companion.PERMISSION_DIALOG_REQ
import com.honey.utils.ViewExtension.TAG
import com.honey.utils.ViewExtension.observeOnce
import com.honey.webservices.ApiConstant
import kotlinx.android.synthetic.main.layout_drawer.tvName

class MainActivity : BaseActivity(), View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    val homeFragment= HomeFragment()
    val bagFragment= BagFragment()
    val notificationFragment= NotificationFragment()
    val favFragment=  FavoriteFragment()
    var doubleBackToExitPressedOnce = false
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
        initControl()
        myObserver()
    }

    override fun onResume() {
        super.onResume()
        Log.e("image","--->"+prefs.image!!)
        lvProfile.visibility=View.VISIBLE
        lvProfileHome.visibility=View.VISIBLE
        CommonUtils.setRoundImage(this,ivProfile,lvProfileHome,prefs.image!!)
        CommonUtils.setRoundImage(this,ivProfileHome,lvProfile,prefs.image!!)
        tvName.setText(prefs.name)
        if(prefs.jwtToken.equals("")) btnLogout.visibility=View.GONE
        else btnLogout.visibility=View.VISIBLE
    }

    override fun init() {
        mainViewModel=ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.defaultAddressApi(prefs.jwtToken!!)
        drawerLayout.setViewScale(GravityCompat.START, 0.95f) //set height scale for main view (0f to 1f)
        drawerLayout.setViewElevation(GravityCompat.START, 80f) //set main view elevation when drawer open (dimension)
        drawerLayout.setViewScrimColor(GravityCompat.START, Color.TRANSPARENT) //set drawer overlay coloe (color)
        drawerLayout.setDrawerElevation(20f) //set drawer elevation (dimension)
        drawerLayout.setContrastThreshold(3f) //set maximum of contrast ratio between white text and background color.
        drawerLayout.setRadius(GravityCompat.START, 50f)
    }

    override fun initControl() {
    bottomNavigationView.setOnNavigationItemSelectedListener(this)
    clMyOrders.setOnClickListener(this)
    clMyProfile.setOnClickListener(this)
    clDeliveryAddress.setOnClickListener(this)
    clPaymentMethod.setOnClickListener(this)
    clContactUs.setOnClickListener(this)
    clAboutUs.setOnClickListener(this)
    ClTerms.setOnClickListener(this)
    ClPrivacyPolicy.setOnClickListener(this)
    ClChangeLanguage.setOnClickListener(this)
    clFaq.setOnClickListener(this)
    ivProfile.setOnClickListener(this)
    clAddress.setOnClickListener(this)
    btnLogout.setOnClickListener(this)
    ivDrawer.setOnClickListener(this)
    if(intent.getStringExtra("cameFrom")!=null){

        if(intent.getStringExtra("cameFrom").equals(MyFirebaseMessageService::class.simpleName))
        {
            CommonUtils.loadFragment(this, this.supportFragmentManager, notificationFragment)
            clAddress.visibility = View.GONE
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = "Notifications"
            bottomNavigationView.menu.getItem(3).setChecked(true)
        }else {
            CommonUtils.loadFragment(this, this.supportFragmentManager, bagFragment)
            clAddress.visibility = View.GONE
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = "Cart"
            bottomNavigationView.menu.getItem(1).setChecked(true)
        }
    }
    else { CommonUtils.loadFragment(this,this.supportFragmentManager,homeFragment) }
    }

    override fun myObserver() {
        mainViewModel.response.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) tvDeliveryAddress.setText(it.default_address!!.address)
            else if(it.status!!.equals(ParamEnum.FAILURE.theValue())) tvDeliveryAddress.text="Please Add Address" })
        mainViewModel.logoutResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) { logoutUser(it) }
            else if(it.status!!.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })
        mainViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun logoutUser(it : CommonModel) {
        Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
        btnLogout.visibility=View.GONE
        prefs.deletePreferences()
        prefs.isFirstTime=true
        prefs.isLanguageFirstTime=true
        CommonUtils.getDeviceToken(prefs)
        CommonUtils.setRoundImage(this,ivProfile,null,prefs.image!!)
        CommonUtils.setRoundImage(this,ivProfileHome,lvProfile,prefs.image!!)
        tvName.setText(prefs.name)
        tvDeliveryAddress.text="Please Add Address"
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.btnLogout ->{ mainViewModel.logoutApi(this,prefs.jwtToken!!) }
            R.id.ivDrawer -> { drawerLayout.openDrawer(GravityCompat.START) }
            R.id.clMyOrders -> { CommonUtils.startActivity(this, OrderActivity::class.java) }
            R.id.clMyProfile -> { CommonUtils.startActivity(this, MyProfileActivity::class.java) }
            R.id.clDeliveryAddress -> { CommonUtils.startActivity(this, DeliveryAddressActivity::class.java) }
            R.id.clContactUs -> {
                intent = Intent(this, ContactUsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.clAboutUs -> { intentWebView("About Us",ApiConstant.ABOUT_US) }
            R.id.ClTerms -> { intentWebView("Terms and Conditions",ApiConstant.TERM_AND_CONDITION) }
            R.id.ClPrivacyPolicy -> { intentWebView("Privacy Policy",ApiConstant.PRIVACY_POLICY) }
            R.id.ClChangeLanguage -> {
                val intent = Intent(this, SelectLanguageActivity::class.java)
                intent.putExtra("cameFrom","Main Screen")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.clFaq -> { CommonUtils.startActivity(this, FAQsActivity::class.java) }
            R.id.ivProfile -> { CommonUtils.startActivity(this, MyProfileActivity::class.java) }
            R.id.clAddress -> {
                prefs.cameFrom= MainActivity::class.simpleName!!
                CommonUtils.startActivity(this, SearchLocationActivity::class.java)
            }
        }
    }

    private fun intentWebView(title: String,apiName:String) {
        val intent= Intent(this, WebviewActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("title",title)
        intent.putExtra("apiName",apiName)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.bottomHome -> {
            drawerLayout.closeDrawers()
            clAddress.visibility=View.VISIBLE
            tvTitle.visibility=View.GONE
            CommonUtils.loadFragment(this,this.supportFragmentManager,homeFragment)
            return true
            }

            R.id.bottomBag ->  {
            drawerLayout.closeDrawers()
            clAddress.visibility=View.GONE
            tvTitle.visibility=View.VISIBLE
            tvTitle.text="Cart"
            CommonUtils.loadFragment(this,this.supportFragmentManager,bagFragment)
            return true
            }

            R.id.bottomFav ->  {
            drawerLayout.closeDrawers()
            if(!prefs.jwtToken.equals(""))
            {
                clAddress.visibility=View.GONE
                tvTitle.visibility=View.VISIBLE
                tvTitle.text="Favorites"
            }
            CommonUtils.loadFragment(this,this.supportFragmentManager,FavoriteFragment())
            return true
            }

            R.id.bottomNotification ->  {
            drawerLayout.closeDrawers()
            if(!prefs.jwtToken.equals(""))
            {
                clAddress.visibility=View.GONE
                tvTitle.visibility=View.VISIBLE
                tvTitle.text="Notifications"
            }
            CommonUtils.loadFragment(this,this.supportFragmentManager,notificationFragment)
            return true
            }
        }
        return false
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (doubleBackToExitPressedOnce) {
            finishAffinity()
        }

        this.doubleBackToExitPressedOnce = true
        CommonUtils.showSnackBar(this,"Please click Back again to exit")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e(TAG(this),"Request Code : "+requestCode+"\n Result Code : "+resultCode)
        when (requestCode) {
            PERMISSION_DIALOG_REQ->{
                val fragment = supportFragmentManager.findFragmentById(R.id.frameLayout);
                fragment!!.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

}

