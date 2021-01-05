package com.frzah.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.frzah.activity.Main.MainActivity
import com.frzah.model.response.failure.ErrorBean
import com.frzah.utils.CommonUtils.Companion.getGsonInstance
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import retrofit2.HttpException

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object ErrorUtil {
    fun handlerGeneralError(context: Context?, throwable: Throwable) {
        throwable.printStackTrace()
        if (context == null) return
        when (throwable) {
            is ConnectException -> showSnackBar(context,"Network Error")
            is SocketTimeoutException -> showSnackBar(context,"Socket Time Out Exception")
            is UnknownHostException -> showSnackBar(context,"No Internet Connection")
            is InternalError -> showSnackBar(context,"Internal Server Error")
            is HttpException -> {
                try {
                    when (throwable.code()) {
                        401 -> {
                            forceLogout(context)
                        }
                        403 -> {
                            displayError(context, throwable)
                        }
                        else -> {
                            displayError(context, throwable)
                        }
                    }
                } catch (exception: Exception) {

                }
            } else -> {
            showSnackBar(context,"Something went wrong")

        }
        }
    }
    fun forceLogout(context: Context) {
        var sharedPreferenceUtil= SharedPreferenceUtil.getInstance(context)
        sharedPreferenceUtil.isLogin=false

        var intent= Intent(context, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun displayError(context: Context, exception: HttpException) {
        try {
            val errorBody = getGsonInstance().
            fromJson(exception.response()?.errorBody()?.charStream(), ErrorBean::class.java)
            Log.e("ErrorMessage", errorBody.message)
            showSnackBar(context,errorBody.message)
        } catch (e: Exception) {
            Log.e("MyExceptions", e.message!!)
            showSnackBar(context,""+exception.message)
        }
    }
}


