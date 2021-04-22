package com.frzah.activity.Payment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.activity.AddNewAddress.AddNewAddressActivity
import com.frzah.activity.Main.MainActivity
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.telr.TelrPayload.getTelrPayoad
import com.frzah.utils.CommonUtils
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.telr.mobile.sdk.activity.WebviewActivity
import com.telr.mobile.sdk.entity.response.status.StatusResponse
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.layout_billing_details.*


class PaymentActivity : BaseActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private lateinit var paymentViewModel: PaymentViewModel
    private var address_id:String?=null
    private var payment_type: String?=""
    private var defaultAddress:ResponseBean?=null
    private var TELR_REQ:Int=12;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
      super.onResume()
      setToolbar(this, getString(R.string.payment))
    }

    override fun init() {
      tvSubTotal.text=prefs.subTotal
      Log.e("tax", prefs.tax)
      if(prefs.tax.equals("0")) clTextFees.visibility=View.GONE
      else clTextFees.visibility=View.VISIBLE
      if(prefs.discount.equals("0")) clDiscount.visibility=View.GONE
      else clDiscount.visibility=View.VISIBLE
      tvShippingCharges.visibility=View.GONE
      tvTax.text=prefs.tax
      tvTotal.text=prefs.total
      tvItems.text=prefs.item
      tvDiscount.text=prefs.discount
      paymentViewModel= ViewModelProviders.of(this).get(PaymentViewModel::class.java)
      paymentViewModel.defaultAddressApi(this, prefs.jwtToken!!)
    }

    override fun initControl() {
        btnConfirmOrder.setOnClickListener(this)
        tvAddress.setOnClickListener(this)
        rgPaymentOption.setOnCheckedChangeListener(this)
        cvDefaultAddress.setOnClickListener(this)
        PushDownAnim.setPushDownAnimTo(cvDefaultAddress).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        PushDownAnim.setPushDownAnimTo(tvAddress).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        PushDownAnim.setPushDownAnimTo(btnConfirmOrder).setScale(PushDownAnim.MODE_SCALE, 0.89f)
    }

    override fun myObserver() {
        paymentViewModel.defaultAddressResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) {
                setDataToUI(it)
            } else if (it.status.equals(ParamEnum.FAILURE.theValue())) {
                CommonUtils.dismissLoadingDialog()
                tvShippingCharges.visibility = View.GONE
                cvDefaultAddress.visibility = View.GONE
                tvAddress.visibility = View.VISIBLE
                mainSV.visibility = View.VISIBLE
            }
        })

        paymentViewModel.directionResponse.observe(this, Observer {
            try {
                Log.e("distance", it.routes!!.get(0).legs!!.get(0).distance!!.text!!)
                if (it.routes!!.get(0).legs!!.get(0).distance!!.text!!.contains("km")) {
                    val kmmString =
                        it.routes!!.get(0).legs!!.get(0).distance!!.text!!.split("km")[0].trim()
                            .toString()
                    val kms = Math.round(kmmString.toDouble())
                    if (kms > 25) {
                        val q = kms / 25
                        tvShippingCharges.text = "" + (1 * 40)
                        rbCOD.visibility = View.GONE
                    } else if (kms <= 25) {
                        tvShippingCharges.text = "20"
                    }
                    tvTotal.text =
                        "" + (prefs.total.toLong() + tvShippingCharges.text.toString().toLong())
                } else if (it.routes!!.get(0).legs!!.get(0).distance!!.text!!.contains("m")) {
                    val kms =
                        it.routes!!.get(0).legs!!.get(0).distance!!.text!!.split("m")[0].trim()
                            .toString()
                    tvShippingCharges.text = "20"
                    tvTotal.text =
                        "" + (prefs.total.toLong() + tvShippingCharges.text.toString().toLong())
                }
            } catch (e: Exception) {

            }

        })
        paymentViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) {
                fireIntent()
            } else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,
                it.message)
        })
        paymentViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })
    }
    private fun fireIntent() {
        finish()
        val intent = Intent(this, MainActivity::class.java)
        Toast.makeText(this, getString(R.string.order_successfully_placed), Toast.LENGTH_LONG).show()
        startActivity(intent)
    }
    private fun setDataToUI(it: CommonModel?) {
        mainSV.visibility=View.VISIBLE
        tvShippingCharges.visibility=View.VISIBLE
        cvDefaultAddress.visibility=View.VISIBLE
        tvAddress.visibility=View.GONE
        paymentViewModel.getDistackInKms(prefs.latitude,
            prefs.longitude,
            "" + it!!.default_address!!.latitude,
            "" + it.default_address!!.longitude)
        defaultAddress=it!!.default_address!!
        if(it!!.default_address!!.type!!.equals(ParamEnum.HOME.theValue())) {
            ivIcon.setImageResource(R.drawable.home_icn)
            tvName.text=getString(R.string.home)
        }
        else {
            ivIcon.setImageResource(R.drawable.office_icn)
            tvName.text=getString(R.string.office_commercial)
        }
        tvPhoneNumber.setText(it.default_address!!.phone)
        tvAddressDefault.setText(it.default_address!!.address)
        address_id= it.default_address!!.id.toString()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnConfirmOrder -> {
                if (checkValidation()) {
                    if (payment_type.equals(ParamEnum.ONLINE.theValue().toString())) {
                        val intent = Intent(this, WebviewActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT)
                        intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getTelrPayoad(this, tvPhoneNumber.text.toString(), defaultAddress, tvTotal.text.toString()))
                        intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.frzah.activity.Payment.PaymentActivity")
                        intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.frzah.activity.Payment.PaymentActivity")
                        intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, true)
                        startActivityForResult(intent,TELR_REQ)
                    } else {
                        paymentViewModel.placeOrderApi(this, prefs.jwtToken!!, payment_type!!, "", prefs.coupon_code!!, tvShippingCharges.text.toString(), address_id!!)
                    }
                }
            }
            R.id.tvAddress -> {
                val intent = Intent(this, AddNewAddressActivity::class.java)
                intent.putExtra("cameFrom", PaymentActivity::class.simpleName)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            R.id.cvDefaultAddress -> {
                val intent = Intent(this, AddNewAddressActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                intent.putExtra("cameFrom", PaymentActivity::class.simpleName)
                intent.putExtra("id", "" + defaultAddress!!.id)
                intent.putExtra("name", "" + defaultAddress!!.name)
                intent.putExtra("phone", "" + defaultAddress!!.phone)
                intent.putExtra("address", "" + defaultAddress!!.address)
                intent.putExtra("street", "" + defaultAddress!!.street)
                intent.putExtra("city", "" + defaultAddress!!.city)
                intent.putExtra("state", "" + defaultAddress!!.state)
                intent.putExtra("type", "" + defaultAddress!!.type)
                intent.putExtra("remark", "" + defaultAddress!!.remark)
                intent.putExtra("country", "" + defaultAddress!!.country)
                intent.putExtra("latitude", "" + defaultAddress!!.latitude)
                intent.putExtra("longitude", "" + defaultAddress!!.longitude)
                intent.putExtra("phone_code", "" + defaultAddress!!.phone_code)
                intent.putExtra("pin_code", "" + defaultAddress!!.pin_code)
                startActivity(intent)
            }
        }
    }
    private fun checkValidation(): Boolean {
        var ret=true
        if(address_id==null)
        {
            ret=false
            showSnackBar(this, getString(R.string.please_add_address_first))
        }else if(payment_type.equals(""))
        {
            ret=false
            showSnackBar(this, getString(R.string.please_select_payment_option))
        }

        return ret
    }
    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(group!!.checkedRadioButtonId)
        {
            R.id.rbOnline -> {
                payment_type = ParamEnum.ONLINE.theValue().toString()
            }
            R.id.rbCOD -> {
                payment_type = ParamEnum.COD.theValue().toString()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            101 ->{ Log.e("data","--> "+data) }
            TELR_REQ -> { getTelrResponse(data,resultCode) } }
        }


    private fun getTelrResponse(data: Intent?, resultCode: Int) {
        val intent = data
        val any: Any? = intent?.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE)
        if (resultCode == RESULT_OK) {
            if (any is StatusResponse) {
                if (any.getAuth() != null) {
                    Toast.makeText(this,getString(R.string.payment_sccessfull),Toast.LENGTH_LONG).show()
                    paymentViewModel.placeOrderApi(this, prefs.jwtToken!!, payment_type!!, any.auth.tranref, prefs.coupon_code!!, tvShippingCharges.text.toString(), address_id!!)
                }

            } else if (any is String) {
                showSnackBar(this,any)
            }
        } else {
            if (any is StatusResponse) {
                if (any.getAuth() != null) {
                    showSnackBar(this, any.auth.message)
                }
            } else if (any is String) {
                showSnackBar(this,any)
            }

        }
    }



}