package com.frzah.activity.SelectLanguage

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.OrderDetail.OrderDetailViewModel
import com.frzah.activity.WalkThrough.WalkThroughActivity
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.frzah.utils.ViewExtension.setLocale
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_select_language.*

class SelectLanguageActivity : BaseActivity(), View.OnClickListener {

    private lateinit var viewModel : SelectLanguageViewModel
    
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)
        setContentView(R.layout.activity_select_language)
        init()
        initControl()
        myObserver()
    }

    override fun onResume() {
        super.onResume()
        prefs.isLanguageFirstTime=true
        prefs.isFirstTime=true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun init() {
        PushDownAnim.setPushDownAnimTo(btnContinue).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        settingBackground(prefs.selectedLanguage)

        viewModel= ViewModelProvider(this).get(SelectLanguageViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun settingBackground(languageCode: String) {
        if(languageCode.equals("en")){
            btnEnglish.backgroundTintList=getColorStateList(R.color.app_theme_organe)
            btnEnglish.setTextColor(ContextCompat.getColor(this,R.color.app_theme_color))
            btnArabic.background=ContextCompat.getDrawable(this,R.drawable.drawable_round_circle_stroke_corners)
            btnArabic.backgroundTintList=null
            btnArabic.setTextColor(ContextCompat.getColor(this,R.color.app_theme_organe))
        }
        else {
            btnEnglish.background=ContextCompat.getDrawable(this,R.drawable.drawable_round_circle_stroke_corners)
            btnEnglish.backgroundTintList=null
            btnEnglish.setTextColor(ContextCompat.getColor(this,R.color.app_theme_organe))
            btnArabic.backgroundTintList=getColorStateList(R.color.app_theme_organe)
            btnArabic.setTextColor(ContextCompat.getColor(this,R.color.app_theme_color))
        }
    }

    override fun initControl() {
        btnContinue.setOnClickListener(this)
        btnEnglish.setOnClickListener(this)
        btnArabic.setOnClickListener(this)
    }

    override fun myObserver() {
        viewModel.response.observe(this,{
            CommonUtils.dismissLoadingDialog()

            if(it.status?.equals(ParamEnum.SUCCESS.theValue())==true) {
               val intent = Intent(this, MainActivity::class.java)
               startActivity(intent)
               finish()
            } else if(it.status?.equals(ParamEnum.FAILURE.theValue())==true){
                CommonUtils.showSnackBar(this,it.message)
            }

        })
        viewModel.error.observe(this,{
            CommonUtils.dismissLoadingDialog()
            ErrorUtil.handlerGeneralError(this,it)

        })

    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnContinue ->{
                if(prefs.id.equals("")){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }else {
                    CommonUtils.showLoadingDialog(this)
                    viewModel.changeLanguageApi(prefs.id!!, prefs.selectedLanguage)
                }
            }

            R.id.btnEnglish ->{
                prefs.selectedLanguage="en"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    settingBackground(prefs.selectedLanguage)
                }
            }

            R.id.btnArabic ->{
                prefs.selectedLanguage="ar"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    settingBackground(prefs.selectedLanguage)
                }
            }
        }

    }


}