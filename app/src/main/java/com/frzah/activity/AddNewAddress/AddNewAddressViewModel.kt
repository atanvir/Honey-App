package com.frzah.activity.AddNewAddress

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.AddressModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddNewAddressViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var editResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()
    var compositeDisposable: CompositeDisposable?= CompositeDisposable()

    fun userAddAddressApi(context: Context, model:AddressModel){
        showLoadingDialog(context as Activity)
        apiInterface.userAddAddress(model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({onSuccess(it)},{onFailure(it)}).let{ compositeDisposable?.add(it)}
    }

    fun userEditAddressApi(context: Context, model:AddressModel) {
        showLoadingDialog(context as Activity)
        apiInterface.userEditddress(model).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onEditSuccess(it) }, { onFailure(it) }).let { compositeDisposable?.add(it)}
    }

    fun onSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.response.value=response
    }

    fun onEditSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.editResponse.value=response
    }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }

    override fun onCleared() {
        super.onCleared()
        if(compositeDisposable?.isDisposed == false){
            compositeDisposable?.clear()
        }
    }


}