package com.frzah.activity.OfferDetail

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OfferDetailViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var addCartResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun offerDetailApi(context: Context, token:String,offerId:String){
        showLoadingDialog(context as Activity)
        apiInterface.offerDetailApi(token=token,offerId=offerId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun addToCartApi(context: Context,product_id: String,seller_id: String,cart_empty:String,token:String,quantity: Long){
        showLoadingDialog(context as Activity)
        apiInterface.addtocart(product_id=product_id,seller_id = seller_id,cart_empty = cart_empty,token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onAddSuccess(it,quantity) }, { onFailure(it) })
    }

    private fun onAddSuccess(response: CommonModel,quantity: Long) {
        dismissLoadingDialog()
        this.addCartResponse.value=response
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