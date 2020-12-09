package com.honey.fragment.Notification

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.dismissLoadingDialog
import com.honey.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import okhttp3.RequestBody

class NotifiactionViewModel : BaseViewModel(){
    var response = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun notficationListApi(context: Context, token:String)
    {
        showLoadingDialog(context as Activity)
        apiInterface.notficationlist(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
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