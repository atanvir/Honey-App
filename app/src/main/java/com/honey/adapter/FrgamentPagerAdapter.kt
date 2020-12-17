package com.honey.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.honey.R
import com.honey.fragment.Order.OrderFragment
import com.honey.fragment.FavAndSearchTab.CommonTabFragment

class FrgamentPagerAdapter(var context: Context,var screen: String,fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment= if(screen.equals("Order")) OrderFragment(position) else CommonTabFragment(position, screen)
    override fun getCount(): Int=2
    override fun getItemPosition(`object`: Any): Int=POSITION_NONE
    override fun getPageTitle(position: Int): CharSequence {
    if(screen.equals("Search")) {
    if (position == 0) return context.getString(R.string.honey_products)
    else return context.getString(R.string.honey_stores)
    }
    else if(screen.equals("Order")) {
    if (position == 0) return context.getString(R.string.upcoming)
    else return context.getString(R.string.history)
    }
    else {
        if (position == 0) return context.getString(R.string.honey_products)
        else return context.getString(R.string.stores)
        }
    }
}