package com.honey.fragment.FavAndSearchTab

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CommonTabViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var onFavResponse = MutableLiveData<CommonModel>()
    var onSearchResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun favListApi(context: Context, token: String,type: String){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.favList(token=token,type=type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun addtowishApi(context: Context,token: String,id:String,type: String)
    {
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.addtowish(token=token,id = id,type=type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFavSuccess(it) }, { onFailure(it) })
    }
     fun searchApi(token: String,search_key:String,type:String,sort_by:String,price_low:String,price_high:String,rating:String)
    {
        apiInterface.search(token=token,search_key=search_key,type=type,sort_by=sort_by,price_low=price_low,price_high=price_high,rating=rating).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSearchSuccess(it) }, { onFailure(it) })
    }



    private fun onFavSuccess(response: CommonModel) {
        this.onFavResponse.value=response
        CommonUtils.dismissLoadingDialog()
    }

    private fun onSearchSuccess(response: CommonModel) {
        this.onSearchResponse.value=response
        CommonUtils.dismissLoadingDialog()
    }


    fun onSuccess(response: CommonModel){
        this.response.value=response
        CommonUtils.dismissLoadingDialog()
    }
    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }

}