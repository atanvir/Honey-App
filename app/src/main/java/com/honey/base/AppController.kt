package com.honey.base

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.honey.utils.SharedPreferenceUtil
import com.honey.utils.ViewExtension
import com.honey.utils.ViewExtension.setLocale

class AppController : Application(){

    override fun onCreate() {
        super.onCreate()
        minstance = this@AppController
    }

    companion object {
       private var minstance: AppController? = null
        fun getInstance(): AppController? { return minstance }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}