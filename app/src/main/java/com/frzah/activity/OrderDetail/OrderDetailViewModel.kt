package com.frzah.activity.OrderDetail

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

class OrderDetailViewModel : BaseViewModel() {
    var response = MutableLiveData<CommonModel>()
    var cancelOrderResponse = MutableLiveData<CommonListModel>()
    var error = MutableLiveData<Throwable>()

    fun orderDetail(context:Context,token:String,order_id:String){
        showLoadingDialog(context as Activity)
        apiInterface.orderDetail(token=token,order_id=order_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun cancelOrderApi(context: Context, token: String, order_id: String) {
        showLoadingDialog(context as Activity)
        apiInterface.cancelOrder(token=token,order_id = order_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onCancelOrderSuccess(it) }, { onFailure(it) })
    }

    fun onCancelOrderSuccess(response: CommonListModel){
        dismissLoadingDialog()
        this.cancelOrderResponse.value=response
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