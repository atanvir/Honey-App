package com.honey.activity.Coupon

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonListModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CouponViewModel: BaseViewModel() {
    var response = MutableLiveData<CommonListModel>()
    var applyResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun couponsListApi(context: Context){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.coupons().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun applyCouponApi(context: Context,token:String,couponCode:String){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.applyCoupon(token,couponCode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onApplySuccess(it) }, { onFailure(it) })
    }


    fun onApplySuccess(response: CommonModel){
        CommonUtils.dismissLoadingDialog()
        this.applyResponse.value=response
    }


    fun onSuccess(response: CommonListModel){
        CommonUtils.dismissLoadingDialog()
        this.response.value=response
    }



    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }
}