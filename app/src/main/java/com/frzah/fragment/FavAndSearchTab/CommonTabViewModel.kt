package com.frzah.fragment.FavAndSearchTab

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CommonTabViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var onFavResponse = MutableLiveData<CommonModel>()
    var onSearchResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun favListApi(context: Context, token: String,type: String){
        showLoadingDialog(context as Activity)
        apiInterface.favList(token=token,type=type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun addtowishApi(context: Context,token: String,id:String,type: String)
    {
        showLoadingDialog(context as Activity)
        apiInterface.addtowish(token=token,id = id,type=type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFavSuccess(it) }, { onFailure(it) })
    }
     fun searchApi(token: String,search_key:String,type:String,sort_by:String,price_low:String,price_high:String,rating:String)
    {
        apiInterface.search(token=token,search_key=search_key,type=type,sort_by=sort_by,price_low=price_low,price_high=price_high,rating=rating).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSearchSuccess(it) }, { onFailure(it) })
    }



    private fun onFavSuccess(response: CommonModel) {
        this.onFavResponse.value=response
        dismissLoadingDialog()
    }

    private fun onSearchSuccess(response: CommonModel) {
        this.onSearchResponse.value=response
        dismissLoadingDialog()
    }


    fun onSuccess(response: CommonModel){
        this.response.value=response
        dismissLoadingDialog()
    }
    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }

}