package com.frzah.telr

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.frzah.R
import com.frzah.base.BaseActivity
import com.telr.mobile.sdk.activity.WebviewActivity
import com.telr.mobile.sdk.entity.response.status.StatusResponse
import kotlinx.android.synthetic.main.activity_transaction_status.*

class TransactionStatusActivity : BaseActivity() {

    companion object{
        private var listner:PaymentResponseListnerss?= null
        fun setListner(listner:PaymentResponseListnerss){
            this.listner=listner
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_status)
        init()
        initControl()
    }

    override fun init() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        loadPaymentResponse()
    }


    override fun initControl() {

    }

    override fun myObserver() {
    }


    private fun loadPaymentResponse() {
        val intent = intent
        tvAmount.text=getString(R.string.sar)+" "+prefs.total
        val any: Any? = intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE)
        if(any is StatusResponse){
            if (any.getAuth() != null) {
                tvPaymentStatus.text="Payment "+any.auth.message
                tvTransactionId.text="Transaction ID: "+any.auth.tranref
                Log.e("status",any.auth.status)
                Log.e("message",any.auth.message)
                Log.e("trace",any.trace)
                Log.e("transactionId",any.auth.tranref)
                listner?.onPaymentCallBack(any.auth.tranref,any.auth.status)
            }

        }else if(any is String){
            tvPaymentStatus.text="Payment "+any
            tvTransactionId.visibility= View.GONE
        }

    }

    interface PaymentResponseListnerss{
        fun onPaymentCallBack(transactionId:String,status:String)
    }



}