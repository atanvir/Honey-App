package com.honey.fragment.Favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.honey.R
import com.honey.adapter.FrgamentPagerAdapter
import com.honey.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*

class FavoriteFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
          return inflater.inflate(R.layout.fragment_favorite,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initControl()
        myObserver()
    }

    override fun init() {
        val pagerAdapter = FrgamentPagerAdapter(requireContext(),"Favorite", requireActivity().supportFragmentManager)
        viewPager.adapter=pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initControl() {
    }

    override fun myObserver() {
    }
}