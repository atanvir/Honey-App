package com.frzah.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.frzah.R
import kotlinx.android.synthetic.main.adapter_walk_throw.view.*

class WalkThrowAdapter(val context: Context,val images: Array<Int>,val descriptions: Array<String>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(context).inflate(R.layout.adapter_walk_throw,container,false)
        view.imageView.setImageResource(images[position])
        val viewPager:ViewPager= container as ViewPager
        viewPager.addView(view,0)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean =view==`object`
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = container.removeView(`object` as View)
    override fun getCount(): Int =images.size
}