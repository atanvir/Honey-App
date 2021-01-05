package com.frzah.activity.HomeFilter

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeFilterViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var filterResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun categoryListApi(context: Context)
    {
        showLoadingDialog(context as Activity)
        apiInterface.categorylist().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun homeFilterApi(context: Context,token:String,latitude:String,longitude:String,rating:String,type:String,delivery_day:String,price_low:String,price_high:String)
    {
        showLoadingDialog(context as Activity)
        apiInterface.homesearch(token=token,latitude=latitude,longitude=longitude,rating=rating,type=type,delivery_day=delivery_day,price_low=price_low,price_high=price_high).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFilterSuccess(it) }, { onFailure(it) })
    }

    fun onSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.response.value=response
    }

    fun onFilterSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.filterResponse.value=response
    }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }

}