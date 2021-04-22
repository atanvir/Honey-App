package com.frzah.activity.OTPVerification

import `in`.aabhasjindal.otptextview.OTPListener
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.activity.Main.MainActivity
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.*
import com.frzah.utils.CommonUtils.Companion.dismissLoadingDialog
import com.frzah.utils.CommonUtils.Companion.getGuestData
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ViewExtension.TAG
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import kotlinx.android.synthetic.main.activity_otp_verification.*
import kotlinx.android.synthetic.main.dialog_verified.*
import java.util.concurrent.TimeUnit

class OTPVerificationActivity : BaseActivity(), View.OnClickListener, DialogInterface.OnCancelListener, OnCompleteListener<AuthResult>, OTPListener {
    private var mAuth:FirebaseAuth?=null
    private var verificationCode = ""
    private var mResendToken: ForceResendingToken? = null
    private lateinit var otpViewModel : OTPVerificationViewModel
    private var dialog: Dialog?=null
    private var product_id:String=""
    private var quantity:String=""
    private var seller_id:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp_verification)
        init()
        initControl()
        myObserver()
        sendOtp()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this,"")
        tvMobileNo.text= intent.getStringExtra("country_code")+" "+intent.getStringExtra("phone_number")
    }

    override fun init() {
        mAuth = FirebaseAuth.getInstance()
        otpViewModel= ViewModelProvider(this).get(OTPVerificationViewModel::class.java)
        if(prefs.jwtToken!!.equals(""))
        {
            product_id=getGuestData(""+ParamEnum.PRODUCT_ID.theValue())
            seller_id=getGuestData(""+ParamEnum.SELLER_ID.theValue())
            quantity=getGuestData(""+ParamEnum.QUANTITY.theValue())
        }
    }


    override fun initControl() {
        ivNext.setOnClickListener(this)
        tvResend.setOnClickListener(this)
        otp_view.otpListener=this
    }

    override fun myObserver() {
        otpViewModel.response.observe(this, Observer {   if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkData(it.signup!!)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })
        otpViewModel.verifyResponse.observe(this, Observer {   if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkData(it!!)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })
        otpViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private var mCallback:PhoneAuthProvider.OnVerificationStateChangedCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            dismissLoadingDialog()
            if(credential?.smsCode!=null) {
                otp_view?.setOTP("" + credential?.smsCode!!)
                signInWithPhoneAuthCredential(credential)
            }else{
//                sendOtp()
            }
        }

        override fun onVerificationFailed(e: FirebaseException) {
            dismissLoadingDialog()
            showSnackBar(this@OTPVerificationActivity,e.message)
        }

        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
            dismissLoadingDialog()
            Log.d("OTPVerificationActivity", "onCodeSent:$verificationId")
            verificationCode = verificationId
            mResendToken = token
        }
    }


    private fun sendOtp() {
        showLoadingDialog(this)
//        mAuth!!.setLanguageCode(prefs.selectedLanguage)
        val options = PhoneAuthOptions.newBuilder(mAuth!!).setPhoneNumber(intent.getStringExtra("country_code") + intent.getStringExtra("phone_number")).setTimeout(60L, TimeUnit.SECONDS).setActivity(this).setCallbacks(mCallback!!).build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun checkData(response: CommonModel) {
        if(response.message.equals(getString(R.string.phone_number_not_exist)))
        {
            Log.e(TAG(this),"product_id : "+product_id)
            Log.e(TAG(this),"seller_id : "+seller_id)
            Log.e(TAG(this),"quantity : "+quantity)
            otpViewModel.signupApi(intent.getStringExtra("phone_number")!!,intent.getStringExtra("country_code")!!,prefs.device_token!!,product_id,seller_id,quantity)

        }else if(response.message?.trim().equals(getString(R.string.login_successfully),ignoreCase = true)){
            dismissLoadingDialog()
            prefs.isLogin=true
            prefs.jwtToken=response.signup!!.token!!
            prefs.phone_code=response.signup.user!!.phoneCode
            prefs.phone= response.signup.user!!.phone
            prefs.id=""+ response.signup.user!!.id
            prefs.image=response.signup.user!!.image
            prefs.name=response.signup.user!!.name
            prefs.email=response.signup.user!!.email
            Toast.makeText(this,getString(R.string.login_successful),Toast.LENGTH_LONG).show()
            GuestData.instance!!.removeAllData()
            val intent= Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun checkData(signupData: ResponseBean) {
        prefs.jwtToken=signupData.token!!
        prefs.phone_code=signupData.user!!.phoneCode
        prefs.phone=signupData.user!!.phone
        prefs.image=signupData.user!!.image
        prefs.id=""+signupData.user!!.id
        prefs.isLogin=true

        dialog = Dialog(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setContentView(R.layout.dialog_verified)
        dialog!!.btnOk.setOnClickListener(this)
        dialog!!.setCancelable(true)
        dialog!!.setOnCancelListener(this)
        dialog!!.show()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.ivNext -> {
                if(verificationCode.equals(""))
                { showSnackBar(this,getString(R.string.wait_for_otp)) }
                else if (otp_view.otp!!.length == 0 || otp_view.otp!!.length < 6) {
                    if (otp_view.otp!!.length == 0) showSnackBar(this, getString(R.string.please_enter_otp))
                    else if (otp_view.otp!!.length < 6) showSnackBar(this, getString(R.string.please_enter_valid_otp))
                } else { signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationCode, otp_view.otp!!)) }
            }

            R.id.btnOk -> {
                GuestData.instance!!.removeAllData()
                Toast.makeText(this,getString(R.string.login_successful),Toast.LENGTH_LONG).show()
                dialog!!.dismiss()
                val intent= Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            R.id.tvResend ->{
                showLoadingDialog(this)
                sendOtp()
            }
        }
    }



    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        showLoadingDialog(this)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(this)
    }

    override fun onCancel(p0: DialogInterface?) {
        p0!!.dismiss()
        Toast.makeText(this,getString(R.string.login_successful),Toast.LENGTH_LONG).show()
        GuestData.instance!!.removeAllData()
        val intent= Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    override fun onComplete(task: Task<AuthResult>) {
        if (task.isSuccessful) {
            Log.e(TAG(this),"product_id : "+product_id)
            Log.e(TAG(this),"seller_id : "+seller_id)
            Log.e(TAG(this),"quantity : "+quantity)
            otpViewModel.verfiyPhoneApi(intent.getStringExtra("phone_number")!!,prefs.device_token!!,product_id,seller_id,quantity)
        } else {
            if (task.exception is FirebaseAuthInvalidCredentialsException) {
            if(!task.exception!!.message.equals(getString(R.string.verfication_code_try_again))) {
                dismissLoadingDialog()
                showSnackBar(this,task!!.exception!!.message)
            }
            }
        }
    }

    override fun onInteractionListener() {

    }

    override fun onOTPComplete(otp: String) {
        ivNext.performClick()
    }
}