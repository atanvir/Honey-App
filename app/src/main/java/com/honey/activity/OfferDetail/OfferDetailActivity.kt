package com.honey.activity.OfferDetail

import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.honey.R
import com.honey.adapter.OfferProductAdapter
import com.honey.base.BaseActivity
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension
import kotlinx.android.synthetic.main.activity_offer_detail.*
import kotlinx.android.synthetic.main.adapter_common_product.view.*

class OfferDetailActivity : BaseActivity() {
    private lateinit var offerDetailViewModel: OfferDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_detail)
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
        tvMRP.paintFlags=tvMRP.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        tvDesc.text="Honey you are buying is extracted from selected natural bee-hives in the trees, you will not get this honey in the market. Original product so comes with 100% money back * Let your kids also Experience the taste of original honey"
        ViewExtension.readMore(this,tvDesc,3)
        offerDetailViewModel=ViewModelProviders.of(this).get(OfferDetailViewModel::class.java)
        //offerDetailViewModel.offerDetailApi(this,prefs.jwtToken!!,intent.getStringExtra("offer_id")!!)
        rvOffers.layoutManager=LinearLayoutManager(this)
        rvOffers.adapter=OfferProductAdapter(this,null)
        rvOffers.scheduleLayoutAnimation()
    }

    override fun initControl() {
    }

    override fun myObserver() {
        offerDetailViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) Log.e(ViewExtension.TAG(this),""+it.message!!)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message)
        })

        offerDetailViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })
    }

}