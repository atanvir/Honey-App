package com.frzah.activity.SelectLanguage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.frzah.R
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.WalkThrough.WalkThroughActivity
import com.frzah.base.BaseActivity
import com.frzah.utils.ViewExtension.setLocale
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_select_language.*

class SelectLanguageActivity : BaseActivity(), View.OnClickListener {
    
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)
        setContentView(R.layout.activity_select_language)
        init()
        initControl()
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

    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnContinue ->{
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
//
//                if(intent.getStringExtra("cameFrom")!=null)
//                {
//                    val intent = Intent(this, MainActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }else {
//                    val intent = Intent(this, WalkThroughActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }
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