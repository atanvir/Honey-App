package com.frzah.base

import androidx.lifecycle.ViewModel
import com.e.carty.webservices.ApiClient
import com.e.carty.webservices.ApiInterface
import com.frzah.utils.ParamEnum

abstract class BaseViewModel : ViewModel(){
    val apiInterface : ApiInterface by lazy { ApiClient.getApiClient(ParamEnum.API_BASE_URL.theValue() as String) }
    val apiGoogleInterface : ApiInterface by lazy { ApiClient.getApiClient(ParamEnum.GOOGLE_MAP_BASE_URL.theValue() as String) }
}

