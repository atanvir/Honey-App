package com.honey.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.e.carty.webservices.ApiClient
import com.e.carty.webservices.ApiInterface
import com.honey.utils.ParamEnum
import com.honey.utils.SharedPreferenceUtil

abstract class BaseViewModel : ViewModel(){
    val apiInterface : ApiInterface by lazy { ApiClient.getApiClient(ParamEnum.API_BASE_URL.theValue() as String) }
    val apiGoogleInterface : ApiInterface by lazy { ApiClient.getApiClient(ParamEnum.GOOGLE_MAP_BASE_URL.theValue() as String) }
}

