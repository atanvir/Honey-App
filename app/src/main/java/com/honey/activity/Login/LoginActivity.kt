package com.honey.activity.Login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.honey.R
import com.honey.activity.OTPVerification.OTPVerificationActivity
import com.honey.base.BaseActivity
import com.honey.utils.CommonUtils
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
        CommonUtils.setToolbar(this,"")
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
                    intent.putExtra("country_code",""+countryCodePicker.defaultCountryCodeWithPlus)
                    intent.putExtra("phone_number",""+edPhoneNumber.text.toString().trim())
                    startActivity(intent)
                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        var ret=true
        Log.e("length",""+edPhoneNumber.text.toString().length);
        if(edPhoneNumber.text.toString().length==0)
        {
            ret=false
            CommonUtils.showSnackBar(this,"Please enter phone number")
        }
        else if(edPhoneNumber.text.toString().length<9)
        {
            ret=false
            CommonUtils.showSnackBar(this,"Please enter valid phone number")
        }
        return ret

    }
}