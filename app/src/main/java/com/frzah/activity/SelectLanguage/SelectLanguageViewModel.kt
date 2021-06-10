package com.frzah.activity.SelectLanguage

import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.model.request.CommonModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SelectLanguageViewModel : BaseViewModel() {
    var response = MutableLiveData<CommonModel>()
    var error = MutableLiveData<Throwable>()

    fun changeLanguageApi(user_id: String,lang: String){
        apiInterface.changeLanguage(user_id,lang).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                        response.value=it
                       },
                       {
                           error.value=it
                       })

    }




}