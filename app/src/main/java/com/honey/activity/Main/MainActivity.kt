
package com.honey.activity.Main

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.honey.R
import com.honey.activity.ContactUs.ContactUsActivity
import com.honey.activity.DeliveryAddress.DeliveryAddressActivity
import com.honey.activity.FAQs.FAQsActivity
import com.honey.activity.MyProfile.MyProfileActivity
import com.honey.activity.Order.OrderActivity
import com.honey.activity.SearchLocation.SearchLocationActivity
import com.honey.activity.SelectLanguage.SelectLanguageActivity
import com.honey.activity.Webview.WebviewActivity
import com.honey.base.BaseActivity
import com.honey.firebase.MyFirebaseMessageService
import com.honey.fragment.Bag.BagFragment
import com.honey.fragment.Favorite.FavoriteFragment
import com.honey.fragment.Home.HomeFragment
import com.honey.fragment.Notification.NotificationFragment
import com.honey.model.request.CommonModel
import com.honey.model.response.success.ResponseBean
import com.honey.utils.CommonUtils.Companion.PERMISSION_DIALOG_REQ
import com.honey.utils.CommonUtils.Companion.getDeviceToken
import com.honey.utils.CommonUtils.Companion.loadFragment
import com.honey.utils.CommonUtils.Companion.setRoundImage
import com.honey.utils.CommonUtils.Companion.showSnackBar
import com.honey.utils.CommonUtils.Companion.startActivity
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.TAG
import com.honey.utils.ViewExtension.findBadgeId
import com.honey.utils.ViewExtension.setLocale
import com.honey.webservices.ApiConstant
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_drawer.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*

class MainActivity : BaseActivity(), View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    var homeFragment: HomeFragment?=null
    val bagFragment= BagFragment()
    val notificationFragment= NotificationFragment()
    val favFragment= FavoriteFragment()
    var doubleBackToExitPressedOnce = false
    private lateinit var mainViewModel: MainViewModel
    var tvBadge:TextView?=null
    var notifcationBroadcastReceiver:BroadcastReceiver?=null
    var isFavSelected=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)
        setContentView(R.layout.activity_main)
        init()
        initControl()
        myObserver()
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(notifcationBroadcastReceiver, IntentFilter("com.honey"))
    }
    override fun onResume() {
        super.onResume()
        lvProfile.visibility=View.VISIBLE
        lvProfileHome.visibility=View.VISIBLE
        Log.e(TAG(this), prefs.image!!)
        setRoundImage(this, ivProfile, lvProfileHome, prefs.image)
        setRoundImage(this, ivProfileHome, lvProfile, prefs.image)
        tvName.setText(prefs.name)
        if(prefs.jwtToken.equals("")) btnLogout.visibility=View.GONE
        else btnLogout.visibility=View.VISIBLE
    }
    override fun init() {
        tvBadge=findBadgeId(this, bottomNavigationView.getChildAt(0) as BottomNavigationMenuView)
        homeFragment=HomeFragment(tvBadge!!)
        mainViewModel=ViewModelProviders.of(this).get(MainViewModel::class.java)
        if(prefs.jwtToken!!.equals("")) tvDeliveryAddress.text = getString(R.string.please_add_address)
        else mainViewModel.defaultAddressApi(prefs.jwtToken!!)
        drawerLayout.setViewScale(getGravity(), 0.95f)
        drawerLayout.setViewElevation(getGravity(), 80f)
        drawerLayout.setViewScrimColor(getGravity(), Color.TRANSPARENT)
        drawerLayout.setDrawerElevation(20f)
        drawerLayout.setContrastThreshold(3f)
        drawerLayout.setRadius(getGravity(), 50f)
        notifcationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                mainViewModel.notificationCountApi(prefs.jwtToken!!)
            }
        }
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
            val intent=Intent(this, OrderActivity::class.java)
            intent.putExtra("body", getIntent().getStringExtra("body"))
            startActivity(intent)
            finish()

        }else {
            isFavSelected=false
            loadFragment(this, this.supportFragmentManager, bagFragment)
            clAddress.visibility = View.GONE
            tvTitle.visibility = View.VISIBLE
            tvTitle.text = getString(R.string.cart)
            bottomNavigationView.menu.getItem(1).setChecked(true)
        }
    }
    else {
        isFavSelected=false
        loadFragment(this, this.supportFragmentManager, homeFragment!!)
    }
    }
    override fun myObserver() {
        mainViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) tvDeliveryAddress.setText(it.default_address!!.address)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) tvDeliveryAddress.text = getString(
                    R.string.please_add_address)
        })
        mainViewModel.logoutResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) { logoutUser(it) }
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })
        mainViewModel.notificationResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkNotificationBadge(it.response)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })
        mainViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })
    }
    private fun getGravity(): Int {
        if(prefs.selectedLanguage.equals("en")) return GravityCompat.START
        else return GravityCompat.END
    }
    private fun checkNotificationBadge(response: ResponseBean?) {
        if(response!!.count.equals("0")) tvBadge!!.visibility=View.GONE
        else tvBadge!!.visibility=View.VISIBLE
        tvBadge!!.text= response.count
    }
    private fun logoutUser(it: CommonModel) {
        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
        btnLogout.visibility=View.GONE
        val langCode=prefs.selectedLanguage
        prefs.deletePreferences()
        prefs.isFirstTime=true
        prefs.isLanguageFirstTime=true
        prefs.selectedLanguage=langCode
        getDeviceToken(prefs)
        setRoundImage(this, ivProfile, null, prefs.image!!)
        setRoundImage(this, ivProfileHome, lvProfile, prefs.image!!)
        tvName.setText(prefs.name)
        tvDeliveryAddress.text=getString(R.string.please_add_address)
    }
    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.btnLogout -> { mainViewModel.logoutApi(this, prefs.jwtToken!!) }
            R.id.ivDrawer -> { drawerLayout.openDrawer(GravityCompat.START) }
            R.id.clMyOrders -> { startActivity(this, OrderActivity::class.java) }
            R.id.clMyProfile -> { startActivity(this, MyProfileActivity::class.java) }
            R.id.clDeliveryAddress -> { startActivity(this, DeliveryAddressActivity::class.java) }
            R.id.clContactUs -> {
                intent = Intent(this, ContactUsActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.clAboutUs -> { intentWebView(getString(R.string.about_us), ApiConstant.ABOUT_US) }
            R.id.ClTerms -> { intentWebView(getString(R.string.term_conditions), ApiConstant.TERM_AND_CONDITION) }
            R.id.ClPrivacyPolicy -> { intentWebView(getString(R.string.privacy_policy), ApiConstant.PRIVACY_POLICY) }
            R.id.ClChangeLanguage -> {
                val intent = Intent(this, SelectLanguageActivity::class.java)
                intent.putExtra("cameFrom", "Main Screen")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
            R.id.clFaq -> { startActivity(this, FAQsActivity::class.java) }
            R.id.ivProfile -> { startActivity(this, MyProfileActivity::class.java) }

            R.id.clAddress -> {
                prefs.cameFrom = MainActivity::class.simpleName!!
                startActivity(this, SearchLocationActivity::class.java)
            }
        }
    }
    private fun intentWebView(title: String, apiName: String) {
        val intent= Intent(this, WebviewActivity::class.java)
        intent.flags= Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("title", title)
        intent.putExtra("apiName", apiName)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.bottomHome -> {
                drawerLayout.closeDrawers()
                clAddress.visibility = View.VISIBLE
                tvTitle.visibility = View.GONE
                isFavSelected=false
                loadFragment(this, this.supportFragmentManager, homeFragment!!)
                return true
            }
            R.id.bottomBag -> {
                drawerLayout.closeDrawers()
                clAddress.visibility = View.GONE
                tvTitle.visibility = View.VISIBLE
                tvTitle.text = getString(R.string.cart)
                isFavSelected=false
                loadFragment(this, this.supportFragmentManager, bagFragment)
                return true
            }
            R.id.bottomFav -> {
                if(!isFavSelected) {
                    isFavSelected=true
                    drawerLayout.closeDrawers()
                    if (!prefs.jwtToken.equals("")) {
                        clAddress.visibility = View.GONE
                        tvTitle.visibility = View.VISIBLE
                        tvTitle.text = getString(R.string.favorites)
                    }
                    loadFragment(this, this.supportFragmentManager, FavoriteFragment())
                    return true
                }
            }
            R.id.bottomNotification -> {
                drawerLayout.closeDrawers()
                if (!prefs.jwtToken.equals("")) {
                    clAddress.visibility = View.GONE
                    tvTitle.visibility = View.VISIBLE
                    tvTitle.text = getString(R.string.notification)
                }
                isFavSelected=false
                tvBadge!!.visibility = View.GONE
                loadFragment(this, this.supportFragmentManager, notificationFragment)
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
        showSnackBar(this, getString(R.string.press_again_to_exit))
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PERMISSION_DIALOG_REQ -> {
                val fragment = supportFragmentManager.findFragmentById(R.id.frameLayout);
                fragment!!.onActivityResult(requestCode, resultCode, data);
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(notifcationBroadcastReceiver)
    }
}

