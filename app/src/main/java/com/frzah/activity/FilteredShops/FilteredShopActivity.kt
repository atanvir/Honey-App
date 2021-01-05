package com.frzah.activity.FilteredShops

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.activity.Search.SearchActivity
import com.frzah.adapter.CommonHomeAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.CommonShopsItemModel
import com.frzah.model.response.success.ProductDetailModel
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.startActivity
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import kotlinx.android.synthetic.main.activity_filtered_shops.*

class FilteredShopActivity : BaseActivity(), CommonHomeAdapter.setOnShopClickListner, View.OnClickListener {
    private lateinit var filteredShopViewModel: FilteredShopViewModel
    var pos:Int?=0
    private var dataList :MutableList<CommonShopsItemModel>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filtered_shops)
        init()
        initControl()
        myObserver()
        setAdapter()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this,getString(R.string.filtered_shops))
    }


    override fun init() {
        filteredShopViewModel=ViewModelProviders.of(this).get(FilteredShopViewModel::class.java)
    }

    override fun initControl() {
        edFindShop.setOnClickListener(this)
    }

    override fun myObserver() {

        filteredShopViewModel.onFavResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkFavData(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message)
        })

        filteredShopViewModel.error.observe(this,Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setAdapter() {
        dataList=ArrayList()
        val data=intent.getParcelableArrayListExtra<ProductDetailModel>("data")
        for(i in 0..data!!.size-1){
            Log.e("",""+data.get(i).rating_count)
            dataList!!.add(CommonShopsItemModel(data.get(i).id!!.toInt(),data.get(i).name,data.get(i).images,data.get(i).delivery_time,data.get(i).rating,data.get(i).rating_count,data.get(i).favourite))
        }

        if(dataList!!.size==0) lvHomeFilter.visibility=View.VISIBLE
        val homeAdapter= CommonHomeAdapter(this,dataList!!,"all",this)
        rvFilterShops.adapter=homeAdapter
        homeAdapter.notifyDataSetChanged()
        rvFilterShops.scheduleLayoutAnimation()
    }


    private fun checkFavData(response: CommonModel?) {
        if(response!!.message.equals(getString(R.string.store_added))) dataList!!.get(pos!!).favourite="yes"
        else if(response.message.equals(getString(R.string.store_removed))) dataList!!.get(pos!!).favourite="no"
        rvFilterShops.adapter!!.notifyItemChanged(pos!!)
    }

    override fun onFav(pos: Int, storeId: String) {
        this.pos=pos
        filteredShopViewModel.addToWishApi(prefs.jwtToken!!,storeId,""+ParamEnum.STORE.theValue())
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.edFindShop -> { startActivity(this, SearchActivity::class.java) }
        }
    }

}