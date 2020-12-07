package com.honey.adapter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.honey.fragment.Order.OrderFragment
import com.honey.fragment.FavAndSearchTab.CommonTabFragment

class FrgamentPagerAdapter(var context: Context,var screen: String,fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        if(screen.equals("Order")) return OrderFragment(position)
        else{
            return CommonTabFragment(position, screen)
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        if(screen.equals("Search")) {
            if (position == 0) return "Honey Product"
            else return "Honey Stores"
        }else if(screen.equals("Order"))
        {
            if (position == 0) return "Upcoming"
            else return "History"
        }
        else
        {
            if (position == 0) return "Honey Products"
            else return "Stores"
        }
    }


}