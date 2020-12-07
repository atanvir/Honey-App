package com.honey.activity.FAQs

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.honey.R
import com.honey.adapter.FAQAdapter
import com.honey.base.BaseActivity
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.observeOnce
import kotlinx.android.synthetic.main.activity_faqs.*

class FAQsActivity : BaseActivity() {
    private lateinit var faQsViewModel: FAQsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqs)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"FAQs")
    }

    override fun init() {
        faQsViewModel=ViewModelProviders.of(this).get(FAQsViewModel::class.java)
        faQsViewModel.faqApi(this)
    }

    override fun initControl() {
    }

    override fun myObserver() {
        faQsViewModel.response.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })
        faQsViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setDataToUi(it: CommonModel) {
        rvFaq.layoutManager=LinearLayoutManager(this)
        rvFaq.adapter=FAQAdapter(this,it.faq!!)
        rvFaq.scheduleLayoutAnimation()
    }
}