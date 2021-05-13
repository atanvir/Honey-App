package com.frzah.activity.SelectLocation

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import com.frzah.base.BaseViewModel
import com.frzah.utils.CommonUtils.Companion.HIDE_INFO_WINDOW
import com.frzah.utils.CommonUtils.Companion.NOT_SERVE_THIS_AREA
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.IOException
import java.util.*


class SelectLocationViewModel : BaseViewModel(){
    var currentAddressSuccess = MutableLiveData<String>()
    var error = MutableLiveData<Throwable>()

    fun getCurrentAddres(context: Context,lat:Double,long:Double,handler: Handler){
      getAddress(context,lat,long,handler).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({ onSuccess(it) }, { onFailure(it) })
    }

    private fun getAddress(context: Context, lat: Double, long: Double, handler: Handler): Observable<String> {
        return Observable.create { emitter ->
            var address = ""
            try
            {
                val addressesList: List<Address> = Geocoder(context, Locale.getDefault()).getFromLocation(lat, long, 1)
                if (!addressesList.isEmpty()) {
                    val addresses = addressesList[0]
                    address = addresses.getAddressLine(0)
                    handler.sendEmptyMessage(HIDE_INFO_WINDOW)
                    emitter.onNext(address)
                } else {
                    handler.sendEmptyMessage(NOT_SERVE_THIS_AREA)
                    address = ""
                    emitter.onNext(address)
                }
            } catch (e: IOException) {
                emitter.onError(e)
            }
         }
        }

    fun onSuccess(it: String) {
        currentAddressSuccess.value=it
    }

    fun onFailure(it : Throwable){
        error.value=it
    }

}