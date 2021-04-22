package com.frzah.activity.WalkThrough

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.frzah.R
import com.frzah.activity.Main.MainActivity
import com.frzah.adapter.WalkThrowAdapter
import com.frzah.base.BaseActivity
import com.frzah.utils.ViewExtension.setLocale
import com.thekhaeng.pushdownanim.PushDownAnim
import com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE
import kotlinx.android.synthetic.main.activity_walk_through.*
import me.kungfucat.viewpagertransformers.DepthPageTransformer

class WalkThroughActivity : BaseActivity(), View.OnClickListener {
    val images=arrayOf(R.drawable.aa,R.drawable.bb,R.drawable.cc)
    val desc= arrayOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setLocale(this)
        setContentView(R.layout.activity_walk_through)
        init()
        initControl()
    }

    override fun onResume() {
        super.onResume()
        prefs.isFirstTime= true
    }

    override fun init() {
        val pagerAdpater=WalkThrowAdapter(this, images, desc)
        viewPager.adapter=pagerAdpater
        viewPager.setPageTransformer(true, DepthPageTransformer())
        dotsIndicator.setViewPager(viewPager)
        PushDownAnim.setPushDownAnimTo(ivNext).setScale(MODE_SCALE, 0.89f)

       // viewPager.addPa
    }

    override fun initControl() {
        ivNext.setOnClickListener(this)
    }

    override fun myObserver() {

    }

    override fun onClick(v: View?) {
      when(v?.id)
      {
        R.id.ivNext -> {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

      }
    }
}