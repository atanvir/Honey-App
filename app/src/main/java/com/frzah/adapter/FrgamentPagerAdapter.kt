package com.frzah.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.frzah.R
import com.frzah.fragment.FavAndSearchTab.CommonTabFragment
import com.frzah.fragment.Order.OrderFragment

class FrgamentPagerAdapter(var context: Context,var screen: String,fm: FragmentManager) : FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {
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

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        //super.destroyItem(container, position, `object`)
    }
}