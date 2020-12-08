package com.honey.activity.ContactUs

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ContactUsViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun contactUsApi(context: Context,data :Map<String,RequestBody>,image: MultipartBody.Part)
    {
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.contactUs(image=image,data=data).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
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