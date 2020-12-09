package com.honey.activity.Order

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.honey.R
import com.honey.adapter.FrgamentPagerAdapter
import com.honey.base.BaseActivity
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.setToolbar
import kotlinx.android.synthetic.main.fragment_home.*

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
        setToolbar(this,getString(R.string.my_order))
    }

    override fun init() {
        val pagerAdapter = FrgamentPagerAdapter(this,"Order", this.supportFragmentManager)
        viewPager.adapter=pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initControl() {
    }

    override fun myObserver() {

    }


}