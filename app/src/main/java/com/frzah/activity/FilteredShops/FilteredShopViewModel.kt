package com.frzah.activity.FilteredShops

import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FilteredShopViewModel : BaseViewModel(){
    var error = MutableLiveData<Throwable>()
    var onFavResponse=MutableLiveData<CommonModel>()

    fun addToWishApi(token: String,id: String,type: String)
    {
        apiInterface.addtowish(token=token,id = id,type = type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFavSuccess(it) }, { onFailure(it) })
    }

    private fun onFavSuccess(response: CommonModel) {
        dismissLoadingDialog()
        this.onFavResponse.value=response
    }


    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }

}