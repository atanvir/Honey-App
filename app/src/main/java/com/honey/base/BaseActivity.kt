package com.honey.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.honey.utils.CommonUtils
import com.honey.utils.SharedPreferenceUtil
import com.honey.utils.ViewExtension
import com.honey.utils.ViewExtension.setLocale

abstract class BaseActivity:AppCompatActivity(){
    val prefs: SharedPreferenceUtil by lazy { SharedPreferenceUtil.getInstance(applicationContext)}
    abstract fun init()
    abstract fun initControl()
    abstract fun myObserver()
}