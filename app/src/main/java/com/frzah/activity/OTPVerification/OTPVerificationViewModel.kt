package com.frzah.activity.OTPVerification

import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OTPVerificationViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var verifyResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun signupApi(phone: String,phone_code: String,deviceToken: String,product_id:String,seller_id:String,quantity:String){
        apiInterface.signup(phone=phone,phone_code=phone_code,deviceToken = deviceToken,product_id=product_id,seller_id=seller_id,quantity=quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun verfiyPhoneApi(phone: String,deviceToken: String,product_id:String,seller_id:String,quantity:String){
        apiInterface.verfiyPhone(phone=phone,deviceToken = deviceToken,product_id=product_id,seller_id=seller_id,quantity=quantity).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onVerfiySuccess(it) }, { onFailure(it) })
    }

    fun onSuccess(response: CommonModel){
        CommonUtils.dismissLoadingDialog()
        this.response.value=response
    }

    fun onVerfiySuccess(response: CommonModel){
        this.verifyResponse.value=response
    }

    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }

}