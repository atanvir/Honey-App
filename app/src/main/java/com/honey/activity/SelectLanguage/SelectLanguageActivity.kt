package com.honey.activity.SelectLanguage

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.honey.R
import com.honey.activity.WalkThrough.WalkThroughActivity
import com.honey.base.BaseActivity
import com.honey.utils.ViewExtension
import com.honey.utils.ViewExtension.setLocale
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_select_language.*

class SelectLanguageActivity : BaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_language)
        init()
        initControl()
    }

    override fun onResume() {
        super.onResume()
        prefs.isLanguageFirstTime=true
        setLocale(this)
    }

    override fun init() {
        PushDownAnim.setPushDownAnimTo(btnContinue).setScale(PushDownAnim.MODE_SCALE, 0.89f)
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
                if(intent.getStringExtra("cameFrom")!=null)
                {
                    finish()
                }else {
                    val intent = Intent(this, WalkThroughActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            R.id.btnEnglish ->{
                ivArabics.visibility-View.GONE
                ivEnglish.visibility=View.VISIBLE
                prefs.selectedLanguage="en"
            }

            R.id.btnArabic ->{
                ivArabics.visibility-View.VISIBLE
                ivEnglish.visibility=View.GONE
                prefs.selectedLanguage="ar"
            }
        }

    }

}