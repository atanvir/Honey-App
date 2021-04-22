package com.frzah.activity.Order

import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.annotation.RequiresApi
import com.frzah.R
import com.frzah.adapter.FrgamentPagerAdapter
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils.Companion.PAST_ORDER_TAB
import com.frzah.utils.CommonUtils.Companion.UPCOMING_ORDER_TAB
import com.frzah.utils.CommonUtils.Companion.setToolbar
import kotlinx.android.synthetic.main.activity_order.*

class OrderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        init()
        initControl()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this, getString(R.string.my_order))
    }

    override fun init() {
        val pagerAdapter = FrgamentPagerAdapter(this, "Order", this.supportFragmentManager)
        viewPager.adapter=pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        Handler().postDelayed(Runnable {
            if(intent!!.getStringExtra("body")!=null){
             val status=intent!!.getStringExtra("body")
             if(status.equals("Your new order has been placed",ignoreCase = true)) viewPager.currentItem = UPCOMING_ORDER_TAB
             else if(status.equals("Your order has been Ready",ignoreCase = true)) viewPager.currentItem = UPCOMING_ORDER_TAB
             else if(status.equals("Your order has been Shipped",ignoreCase = true)) viewPager.currentItem = UPCOMING_ORDER_TAB
             else if(status.equals("Your order has been Delivered",ignoreCase = true)) viewPager.currentItem = PAST_ORDER_TAB
             else if(status.equals("Your order has been cancelled",ignoreCase = true)) viewPager.currentItem = PAST_ORDER_TAB
            }
       }, 100)
    }

    override fun initControl() {
    }

    override fun myObserver() {

    }


}