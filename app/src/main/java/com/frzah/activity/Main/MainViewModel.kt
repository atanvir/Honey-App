package com.frzah.activity.Main

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var notificationResponse = MutableLiveData<CommonModel>()
    var logoutResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun defaultAddressApi( token:String){
        apiInterface.defaultAddress(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun notificationCountApi(token:String){
        apiInterface.notificationToken(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onNotificationSuccess(it) }, { onFailure(it) })
    }


    fun logoutApi(context: Context,token: String)
    {
        showLoadingDialog(context as Activity)
        apiInterface.logout(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onLogoutSuccess(it) }, { onFailure(it) })
    }

    fun onSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.response.value=response
    }

    fun onNotificationSuccess(response: CommonModel){
        this.notificationResponse.value=response
    }

    fun onLogoutSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.logoutResponse.value=response
    }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }

}