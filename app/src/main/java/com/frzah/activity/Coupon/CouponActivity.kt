package com.frzah.activity.Coupon

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.adapter.CouponAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonListModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_coupon.*
import java.util.*

class CouponActivity : BaseActivity(), View.OnClickListener, CouponAdapter.setOnCouponCodeClickListner {
    private lateinit var couponViewModel: CouponViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coupon)
        init()
        initControl()
        myObserver()
}

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this,getString(R.string.apply_coupon))
    }

    override fun init() {
        couponViewModel= ViewModelProviders.of(this).get(CouponViewModel::class.java)
        couponViewModel.couponsListApi(this)
    }

    override fun initControl() {
        btnApply.setOnClickListener(this)
        PushDownAnim.setPushDownAnimTo(btnApply).setScale(PushDownAnim.MODE_SCALE, 0.89f)
    }

    override fun myObserver() {
        couponViewModel.response.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })

        couponViewModel.applyResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkResponse(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })

        couponViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun checkResponse(it: CommonModel?) {
        setResult(RESULT_OK)
        finish()
    }

    private fun setDataToUI(it: CommonListModel?) {
    rvCoupon.layoutManager=LinearLayoutManager(this)
    Collections.shuffle(it!!.response!!)
    rvCoupon.adapter=CouponAdapter(this,it!!.response!!,this)
    rvCoupon.scheduleLayoutAnimation()
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnApply -> {
            if(!edCoupon.text.toString().equals("")) couponViewModel.applyCouponApi(this,prefs.jwtToken!!,edCoupon.text.toString())
            else showSnackBar(this,getString(R.string.please_enter_coupon_code_first))
            }
        }
   }

    override fun setCode(couponCode: String) {
        edCoupon.setText(couponCode)
    }
}