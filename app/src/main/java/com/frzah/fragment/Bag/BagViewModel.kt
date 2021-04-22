package com.frzah.fragment.Bag

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers



class BagViewModel : BaseViewModel()
{
    var response = MutableLiveData<CommonModel>()
    var removeCartResponse = MutableLiveData<CommonModel>()
    var removeCouponResponse = MutableLiveData<CommonModel>()
    var updateCartResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun cartListApi(token: String,cart_id:String,quantity:String){
        apiInterface.cartlist(token=token,cart_id =cart_id,quantity = quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun removeToCartApi(context: Context, productId: String,token:String,quantity: Int){
        showLoadingDialog(context as Activity)
        apiInterface.removetocart(productId=productId,token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onRemoveSuccess(it) }, { onFailure(it) })
    }

    fun updateCartApi(context: Context, productId: String,token:String,quantity: Int){
        showLoadingDialog(context as Activity)
        apiInterface.updatecart(productId=productId,token = token,quantity=quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onUpdateSuccess(it) }, { onFailure(it) })
    }

    fun removeCouponApi(context: Context, token: String){
        showLoadingDialog(context as Activity)
        apiInterface.removeCoupon(token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onRemoveCouponSuccess(it) }, { onFailure(it) })
    }

    private fun onUpdateSuccess(response: CommonModel) {
        this.updateCartResponse.value=response
    }

    private fun onRemoveCouponSuccess(response: CommonModel) {
        this.removeCouponResponse.value=response
    }

    private fun onRemoveSuccess(response: CommonModel) {
        this.removeCartResponse.value=response
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