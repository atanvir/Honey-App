package com.honey.activity.Splash

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import com.honey.R
import com.honey.activity.Main.MainActivity
import com.honey.activity.SelectLanguage.SelectLanguageActivity
import com.honey.activity.WalkThrough.WalkThroughActivity
import com.honey.base.BaseActivity
import com.honey.firebase.MyFirebaseMessageService
import com.honey.firebase.NotificationHelper
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.DELAY_FOR_NEXT_SCREEN
import com.honey.utils.CommonUtils.Companion.getDeviceToken
import com.honey.utils.GuestData
import com.honey.utils.ViewExtension.setLocale
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity: BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
        initControl()
        myObserver()
    }

    override fun onResume() {
        super.onResume()
        getDeviceToken(prefs)
        Log.e("allData","-->>"+GuestData.instance!!.allData)

    }


    override fun init() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        startVideo()
    }

    override fun initControl() {
    }

    override fun myObserver() {

    }

    private fun startVideo() {
        try {
            val video: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.splash)
            videoview.setVideoURI(video)
            videoview.setOnCompletionListener { startIntent() }
            videoview.start()
        } catch (ex: Exception) {
            startIntent()
        }
    }

    private fun startIntent() {
        Handler(Looper.getMainLooper()).postDelayed({
            if(prefs.isLanguageFirstTime!! && prefs.isFirstTime!!)
            {
                val intent=Intent(this,MainActivity::class.java)
                if(intent.getStringExtra("title")!=null) {intent.putExtra("cameFrom",MyFirebaseMessageService::class.simpleName)}
                startActivity(intent)
                finish()
            }else if(prefs.isLanguageFirstTime!!  && !(prefs.isFirstTime!!))
            {
                startActivity(Intent(this, WalkThroughActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, SelectLanguageActivity::class.java))
                finish()
            }
        },DELAY_FOR_NEXT_SCREEN)
    }


}