package com.frzah.activity.SearchLocation

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel : BaseViewModel()
 {
    var response = MutableLiveData<CommonModel>()
    var removeAddressResponse = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun userAddressApi(context: Context, token:String){
        showLoadingDialog(context as Activity)
        apiInterface.useraddress(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

     fun removeAddressApi(context: Context, addressId: String, token: String) {
         showLoadingDialog(context as Activity)
         apiInterface.removeAddress(addressId=addressId,token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onRemoveSuccess(it) }, { onFailure(it) })

     }



     fun onSuccess(response: CommonModel){
        dismissLoadingDialog()
        this.response.value=response
    }

     fun onRemoveSuccess(response: CommonModel){
         dismissLoadingDialog()
         this.removeAddressResponse.value=response
     }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }


 }