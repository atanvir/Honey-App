package com.honey.fragment.Home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.carty.webservices.ApiClient
import com.e.carty.webservices.ApiInterface
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.dismissLoadingDialog
import com.honey.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel:BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()
    var onFavResponse=MutableLiveData<CommonModel>()


    fun homeProductApi(context: Context,token: String,latitude:Double,longitude:Double){
        showLoadingDialog(context as Activity)
        apiInterface.homeProducts(token=token,latitude=latitude,longitude = longitude).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }



    fun addToWishApi(token: String,id: String,type: String)
    {
        apiInterface.addtowish(token=token,id = id,type = type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFavSuccess(it) }, { onFailure(it) })

    }

    private fun onFavSuccess(response: CommonModel) {
        dismissLoadingDialog()
        this.onFavResponse.value=response
    }



    fun onSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.response.value=response
    }
    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }

}
