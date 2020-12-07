package com.honey.fragment.Bag

import android.R
import android.app.Activity
import android.content.Context
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.e.carty.webservices.ApiClient
import com.e.carty.webservices.ApiInterface
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
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
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.removetocart(productId=productId,token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onRemoveSuccess(it) }, { onFailure(it) })
    }

    fun updateCartApi(context: Context, productId: String,token:String,quantity: Int){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.updatecart(productId=productId,token = token,quantity=quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onUpdateSuccess(it) }, { onFailure(it) })
    }

    fun removeCouponApi(context: Context, token: String){
        CommonUtils.showLoadingDialog(context as Activity)
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
        CommonUtils.dismissLoadingDialog()
    }
    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }

}