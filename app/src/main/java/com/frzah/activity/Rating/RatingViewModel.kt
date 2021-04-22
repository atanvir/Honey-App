package com.frzah.activity.Rating

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonListModel
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class RatingViewModel: BaseViewModel() {
    var response = MutableLiveData<CommonListModel>()
    var error = MutableLiveData<Throwable>()

    fun orderRatingApi(context: Context, token:String, order_id:String,rate_on_product:String,rate_on_seller:String,comment:String) {
        showLoadingDialog(context as Activity)
        apiInterface.orderRating(token=token,order_id=order_id,rate_on_product=rate_on_product,rate_on_seller=rate_on_seller,comment=comment).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun onSuccess(response: CommonListModel){
        dismissLoadingDialog()
        this.response.value=response
    }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }
}