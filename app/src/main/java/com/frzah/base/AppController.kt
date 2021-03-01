package com.frzah.base

import android.app.Application
import android.content.Context
import com.telr.mobile.sdk.TelrApplication

class AppController : TelrApplication() {

    override fun onCreate() {
        super.onCreate()
        minstance = this@AppController
    }

    companion object {
      private var minstance: AppController? = null
      fun getInstance(): AppController? { return minstance }
    }

}