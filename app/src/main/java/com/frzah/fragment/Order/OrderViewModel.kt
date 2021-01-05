package com.frzah.fragment.Order

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonListModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

 class OrderViewModel : BaseViewModel(){
    var upcomingResponse = MutableLiveData<CommonListModel>()
    var pastResponse = MutableLiveData<CommonListModel>()
    var reOrderResponse = MutableLiveData<CommonListModel>()
    var error = MutableLiveData<Throwable>()

    fun upcomingOrderApi(context: Context, token:String)
    {
        showLoadingDialog(context as Activity)
        apiInterface.upcomingOrder(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onUpcomingSuccess(it) }, { onFailure(it) })
    }


    fun pastOrdersApi(context: Context, token:String)
    {
        showLoadingDialog(context as Activity)
        apiInterface.pastOrder(token=token).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onPastSuccess(it) }, { onFailure(it) })
    }


    fun reorderApi(context: Context, token: String, order_id: String) {
        showLoadingDialog(context as Activity)
        apiInterface.reorder(token=token,order_id = order_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onReOrderSuccess(it) }, { onFailure(it) })
    }

    private fun onReOrderSuccess(response: CommonListModel) {
        this.reOrderResponse.value=response
    }


    fun onUpcomingSuccess(response: CommonListModel){
        dismissLoadingDialog()
        this.upcomingResponse.value=response
    }



    fun onPastSuccess(response: CommonListModel){
        dismissLoadingDialog()
        this.pastResponse.value=response
    }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }



}