package com.frzah.activity.MyProfile

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class MyProfileViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var editProfileResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun userProfileApi(context: Context,token:String){
        showLoadingDialog(context as Activity)
        apiInterface.userprofile(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun userEditProfileApi(context: Context,image: MultipartBody.Part?,requestBody: Map<String,RequestBody>)
    {
        showLoadingDialog(context as Activity)
        apiInterface.userEditProfile(image= image,requestBody= requestBody).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onEditResponse(it) }, { onFailure(it) })
    }

    private fun onEditResponse(response: CommonModel){
        dismissLoadingDialog()
        this.editProfileResponse.value=response

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