package com.honey.activity.ProductDetail

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.e.carty.webservices.ApiClient
import com.e.carty.webservices.ApiInterface
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ProductDetailViewModel : BaseViewModel() {
    var response = MutableLiveData<CommonModel>()
    var quantity = MutableLiveData<Long>()
    var removeCartResponse = MutableLiveData<CommonModel>()
    var updateCartResponse = MutableLiveData<CommonModel>()
    var addCartResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()


    fun productDetailApi(context: Context, productId: String,token:String){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.productDetail(productId=productId,token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun removeToCartApi(context: Context, productId: String,token:String,quantity: Long){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.removetocart(productId=productId,token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onRemoveSuccess(it,quantity) }, { onFailure(it) })
    }

    fun updateCartApi(context: Context, productId: String,token:String,quantity: Int){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.updatecart(productId=productId,token = token,quantity=quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onUpdateSuccess(it,quantity.toLong()) }, { onFailure(it) })
    }

    fun addToCartApi(context: Context,product_id: String,seller_id: String,cart_empty:String,token:String,quantity: Long){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.addtocart(product_id=product_id,seller_id = seller_id,cart_empty = cart_empty,token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onAddSuccess(it,quantity) }, { onFailure(it) })
    }

    private fun onAddSuccess(response: CommonModel,quantity: Long) {
        CommonUtils.dismissLoadingDialog()
        this.addCartResponse.value=response
        this.quantity.value=quantity
    }

    private fun onUpdateSuccess(response: CommonModel,quantity: Long) {
        CommonUtils.dismissLoadingDialog()
        this.updateCartResponse.value=response
        this.quantity.value=quantity
    }

    private fun onRemoveSuccess(response: CommonModel,quantity:Long) {
        CommonUtils.dismissLoadingDialog()
        this.removeCartResponse.value=response
        this.quantity.value=quantity
    }


    fun onSuccess(response: CommonModel){
        CommonUtils.dismissLoadingDialog()
        this.response.value=response
    }
    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }


}