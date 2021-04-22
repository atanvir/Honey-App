package com.frzah.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.frzah.R
import com.frzah.model.response.success.BannerModel
import com.frzah.utils.CommonUtils.Companion.setRoundImage

class HomeBannerAdapter(val context: Context, val data: List<BannerModel>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view=LayoutInflater.from(context!!).inflate(R.layout.adapter_home_banner,container,false)
        setRoundImage(context,view.findViewById(R.id.ivBanner),view.findViewById(R.id.lvBanners),data.get(position).image!!)
        val viewPager:ViewPager= container as ViewPager
        viewPager.addView(view,0)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean=view==`object`
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) = container.removeView(`object` as View)
    override fun getCount(): Int =data.size
}