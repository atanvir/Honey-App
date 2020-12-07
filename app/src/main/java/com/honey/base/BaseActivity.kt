package com.honey.base

import androidx.appcompat.app.AppCompatActivity
import com.honey.utils.SharedPreferenceUtil

abstract class BaseActivity:AppCompatActivity(){
    val prefs: SharedPreferenceUtil by lazy { SharedPreferenceUtil.getInstance(applicationContext)}
    abstract fun init()
    abstract fun initControl()
    abstract fun myObserver()
}