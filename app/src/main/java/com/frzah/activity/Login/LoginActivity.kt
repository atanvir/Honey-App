package com.frzah.activity.Login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.frzah.R
import com.frzah.activity.OTPVerification.OTPVerificationActivity
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ParamEnum
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
     setContentView(R.layout.activity_login)
     init()
     initControl()
     myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        if(prefs.jwtToken!!.equals(""))
        {
            Log.e("product_id",CommonUtils.getGuestData("" + ParamEnum.PRODUCT_ID.theValue()))
            Log.e("seller_id",CommonUtils.getGuestData("" + ParamEnum.SELLER_ID.theValue()))
            Log.e("quantity",CommonUtils.getGuestData("" + ParamEnum.QUANTITY.theValue()))
        }
        setToolbar(this,"")
    }
    override fun init() {
        PushDownAnim.setPushDownAnimTo(btnNext).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        countryCodePicker.setAutoDetectedCountry(true)
    }

    override fun initControl() {
        btnNext.setOnClickListener(this)
    }

    override fun myObserver() {
    }


    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnNext -> {
            if(checkValidation()) {
            val intent= Intent(this, OTPVerificationActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("country_code",""+countryCodePicker.selectedCountryCodeWithPlus)
            intent.putExtra("phone_number",""+edPhoneNumber.text.toString().trim())
            startActivity(intent)
            }
            }
        }
    }

    private fun checkValidation(): Boolean {
        var ret=true
        if(edPhoneNumber.text.toString().length==0)
        {
            ret=false
            showSnackBar(this,getString(R.string.please_enter_phone_number))
        }
        else if(edPhoneNumber.text.toString().length<9)
        {
            ret=false
            showSnackBar(this,getString(R.string.please_enter_phone_number))
        }
        return ret
    }
}