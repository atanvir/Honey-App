package com.frzah.activity.Payment

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class PaymentViewModel : BaseViewModel(){
    var defaultAddressResponse = MutableLiveData<CommonModel>()
    var directionResponse = MutableLiveData<CommonModel>()
    var response = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun defaultAddressApi(context: Context, token:String){
        showLoadingDialog(context as Activity)
        apiInterface.defaultAddress(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onDefaultAddrressSuccess(it) }, { onFailure(it) })
    }

    fun placeOrderApi(context: Context, token:String, paymentType: String, transcation_id: String, coupon_code: String,shipping_charges:String, address_id: String) {
        showLoadingDialog(context as Activity)
        apiInterface.placeOrder(token=token,paymentType=paymentType,transcation_id=transcation_id,coupon_code=coupon_code,shipping_charges = shipping_charges,address_id=address_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })

    }

    fun getDistackInKms(sellerLatitute:String,sellerLongtitde:String,deliveryLat:String,deliveryLong:String){
        apiGoogleInterface.getDirectionApi(sellerLatitute+","+sellerLongtitde,deliveryLat+","+deliveryLong).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onDistaceSuccess(it) }, { onFailure(it) })
    }

    private fun onDistaceSuccess(response: CommonModel) {
        dismissLoadingDialog()
        this.directionResponse.value=response
    }




    fun onDefaultAddrressSuccess(response: CommonModel){
        this.defaultAddressResponse.value=response
    }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }


    private fun onSuccess(response: CommonModel) {
        dismissLoadingDialog()
        this.response.value=response

    }

}
