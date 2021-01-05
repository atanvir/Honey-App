package com.frzah.activity.Coupon

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonListModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CouponViewModel: BaseViewModel() {
    var response = MutableLiveData<CommonListModel>()
    var applyResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun couponsListApi(context: Context){
        showLoadingDialog(context as Activity)
        apiInterface.coupons().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun applyCouponApi(context: Context,token:String,couponCode:String){
        showLoadingDialog(context as Activity)
        apiInterface.applyCoupon(token,couponCode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onApplySuccess(it) }, { onFailure(it) })
    }





    fun onApplySuccess(response: CommonModel){
        dismissLoadingDialog()
        this.applyResponse.value=response
    }


    fun onSuccess(response: CommonListModel){
        dismissLoadingDialog()
        this.response.value=response
    }



    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }
}