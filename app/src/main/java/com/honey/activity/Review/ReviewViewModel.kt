package com.honey.activity.Review

import android.app.Activity
import android.content.Context
import android.telephony.mbms.StreamingServiceInfo
import androidx.lifecycle.MutableLiveData
import com.honey.base.BaseViewModel
import com.honey.model.request.CommonListModel
import com.honey.model.request.ReviewModel
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.dismissLoadingDialog
import com.honey.utils.CommonUtils.Companion.showLoadingDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ReviewViewModel : BaseViewModel(){
    var response = MutableLiveData<ReviewModel>()
    var reviewResponse = MutableLiveData<CommonListModel>()
    var error = MutableLiveData<Throwable>()

    fun allReviewsApi(context:Context,type: String,id:String){
        showLoadingDialog(context as Activity)
        apiInterface.allReviews(type,id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    fun addReviewApi(context:Context,token:String,review:String,type:String,product_id:String){
        showLoadingDialog(context as Activity)
        apiInterface.addReview(token=token,review=review,type=type,product_id=product_id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onAddReviewSuccess(it) }, { onFailure(it) })
    }

    private fun onAddReviewSuccess(response: CommonListModel) {
        dismissLoadingDialog()
        this.reviewResponse.value=response
    }

    fun onSuccess(response: ReviewModel){
        dismissLoadingDialog()
        this.response.value=response
    }

    fun onFailure(it : Throwable){
        dismissLoadingDialog()
        error.value=it
    }
}