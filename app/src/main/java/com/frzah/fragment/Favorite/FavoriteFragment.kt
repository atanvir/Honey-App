package com.frzah.fragment.Favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.frzah.R
import com.frzah.adapter.FrgamentPagerAdapter
import com.frzah.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_favorite.*

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

    override fun onResume() {
        super.onResume()
    }

    override fun initControl() {
    }

    override fun myObserver() {
    }
}