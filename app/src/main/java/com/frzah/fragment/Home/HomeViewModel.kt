package com.frzah.fragment.Home

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel:BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var notificationResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()
    var onFavResponse=MutableLiveData<CommonModel>()


    fun homeProductApi(context: Context,token: String,latitude:Double,longitude:Double){
        showLoadingDialog(context as Activity)
        apiInterface.homeProducts(token=token,latitude=latitude,longitude = longitude).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it,token=token) }, { onFailure(it) })
    }

    fun notificationCountApi(token:String){
        apiInterface.notificationToken(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onNotificationSuccess(it) }, { onFailure(it) })
    }

    fun addToWishApi(token: String,id: String,type: String)
    {
        apiInterface.addtowish(token=token,id = id,type = type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFavSuccess(it) }, { onFailure(it) })
    }

    fun onNotificationSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.notificationResponse.value=response
    }

    private fun onFavSuccess(response: CommonModel) {
        dismissLoadingDialog()
        this.onFavResponse.value=response
    }

    fun onSuccess(response: CommonModel,token:String){
        this.response.value=response
        if(!token.equals("")) notificationCountApi(token=token)
        else dismissLoadingDialog()
    }
    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }

}
