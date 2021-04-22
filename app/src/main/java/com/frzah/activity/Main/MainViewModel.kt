package com.frzah.activity.Main

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel : BaseViewModel(){

    var disposable:CompositeDisposable?=null
    var response = MutableLiveData<CommonModel>()
    var notificationResponse = MutableLiveData<CommonModel>()
    var logoutResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun defaultAddressApi( token:String){
        apiInterface.defaultAddress(token=token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onSuccess(it) }, { onFailure(it) }).let {
            disposable?.add(it)
        }
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

    override fun onCleared() {
        disposable?.dispose()
        super.onCleared()
    }

}