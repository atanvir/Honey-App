package com.honey.activity.HomeFilter

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonListModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeFilterViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var filterResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun categoryListApi(context: Context)
    {
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.categorylist().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun homeFilterApi(context: Context,token:String,latitude:String,longitude:String,rating:String,type:String,delivery_day:String,to:String,from:String)
    {
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.homesearch(token=token,latitude=latitude,longitude=longitude,rating=rating,type=type,delivery_day=delivery_day,to=to,from=from).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFilterSuccess(it) }, { onFailure(it) })
    }

    fun onSuccess(response: CommonModel){
        CommonUtils.dismissLoadingDialog()
        this.response.value=response
    }

    fun onFilterSuccess(response: CommonModel){
        CommonUtils.dismissLoadingDialog()
        this.filterResponse.value=response
    }

    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }

}