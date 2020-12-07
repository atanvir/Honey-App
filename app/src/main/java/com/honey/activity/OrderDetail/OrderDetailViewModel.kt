package com.honey.activity.OrderDetail

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonListModel
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class OrderDetailViewModel : BaseViewModel() {
    var response = MutableLiveData<CommonModel>()
    var cancelOrderResponse = MutableLiveData<CommonListModel>()
    var error = MutableLiveData<Throwable>()

    fun orderDetail(context:Context,token:String,order_id:String){
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.orderDetail(token=token,order_id=order_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun cancelOrderApi(context: Context, token: String, order_id: String) {
        CommonUtils.showLoadingDialog(context as Activity)
        apiInterface.cancelOrder(token=token,order_id = order_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onCancelOrderSuccess(it) }, { onFailure(it) })
    }

    fun onCancelOrderSuccess(response: CommonListModel){
        CommonUtils.dismissLoadingDialog()
        this.cancelOrderResponse.value=response
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