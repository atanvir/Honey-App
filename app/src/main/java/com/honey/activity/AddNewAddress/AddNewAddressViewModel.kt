package com.honey.activity.AddNewAddress

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honey.base.BaseViewModel
import com.honey.model.request.AddressModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddNewAddressViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var editResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun userAddAddressApi(context: Context, model:AddressModel){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.userAddAddress(model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun userEditAddressApi(context: Context, model:AddressModel) {
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.userEditddress(model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onEditSuccess(it) }, { onFailure(it) })
    }

    fun onSuccess(response: CommonModel){
        CommonUtils.dismissLoadingDialog()
        this.response.value=response
    }

    fun onEditSuccess(response: CommonModel){
        CommonUtils.dismissLoadingDialog()
        this.editResponse.value=response
    }

    fun onFailure(it : Throwable){
        CommonUtils.dismissLoadingDialog()
        error.value=it
    }


}