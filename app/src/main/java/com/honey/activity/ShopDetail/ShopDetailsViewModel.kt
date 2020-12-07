package com.honey.activity.ShopDetail

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

class ShopDetailsViewModel : BaseViewModel() {
    var response = MutableLiveData<CommonModel>()
    var onProductResponse = MutableLiveData<CommonModel>()
    var onCartResponse = MutableLiveData<CommonModel>()
    var onFavResponse=MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun storeDetailApi(context: Context,storeId: String,token: String){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.storeDetail(storeId=storeId,token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it,storeId,token) }, { onFailure(it) })
    }

    fun productsTypeApi(context: Context,storeId: String,type:String,token: String){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.productsType(storeId=storeId,type = type,token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onProductSuccess(it) }, { onFailure(it) })
    }

    fun addToCartApi(product_id: String,seller_id: String,cart_empty:String,token:String){
        apiInterface.addtocart(product_id=product_id,seller_id = seller_id,cart_empty = cart_empty,token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onCartSuccess(it) }, { onFailure(it) })
    }

    fun addToWishApi(token: String,id: String,type: String)
    {
        apiInterface.addtowish(token=token,id = id,type = type).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onFavSuccess(it) }, { onFailure(it) })

    }

    private fun onFavSuccess(response: CommonModel) {
        CommonUtils.dismissLoadingDialog()
        this.onFavResponse.value=response
    }

    private fun onCartSuccess(response: CommonModel) {
        CommonUtils.dismissLoadingDialog()
        this.onCartResponse.value=response
    }


    fun onSuccess(response: CommonModel, storeId: String,token: String){
        this.response.value=response
        if(response!!.response!!.storeModel!!.type!!.size>0) apiInterface.productsType(storeId=storeId,type= response!!.response!!.storeModel!!.type!!.get(0).toLowerCase(),token = token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onProductSuccess(it) }, { onFailure(it) })
        else CommonUtils.dismissLoadingDialog()
    }

    private fun onProductSuccess(response: CommonModel) {
        CommonUtils.dismissLoadingDialog()
        this.onProductResponse.value=response
    }

    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }

}