package com.frzah.base

import android.Manifest
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.frzah.R
import com.frzah.utils.CommonUtils
import com.frzah.utils.SharedPreferenceUtil
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.telr.mobile.sdk.TelrApplication

abstract class BaseActivity:AppCompatActivity(){
    val prefs: SharedPreferenceUtil by lazy { SharedPreferenceUtil.getInstance(applicationContext)}
    abstract fun init()
    abstract fun initControl()
    abstract fun myObserver()
    }
