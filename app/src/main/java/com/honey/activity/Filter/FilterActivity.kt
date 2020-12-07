package com.honey.activity.Filter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.honey.R
import com.honey.adapter.FilterItemAdapter
import com.honey.adapter.FilterRatingAdapter
import com.honey.base.BaseActivity
import com.honey.model.request.CommonModel
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.observeOnce
import com.stfalcon.pricerangebar.model.BarEntry
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*

class FilterActivity : BaseActivity(), View.OnClickListener, FilterRatingAdapter.setOnItemClickListner, FilterItemAdapter.setOnFilterItemClickListner {
    val honeyType= arrayListOf("Natural","Arabian","KSA","Dark","Acacia","Black Forest")
    val sort = arrayListOf("Popular","Free Delivery","Nearest","Delivery time","Price Range")
    val ratingList= arrayListOf("5","4","3","2","1")
    private lateinit var filterViewModel:FilterViewModel
    var type: String?=""
    var sort_by: String?=""
    var price_low: String?="50"
    var price_high: String?="5000"
    var rating: String?=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"Filter")
        type=intent.getStringExtra(""+ParamEnum.TYPE.theValue())
        sort_by=intent.getStringExtra(""+ParamEnum.SORT_BY.theValue())
        price_low=intent.getStringExtra(""+ParamEnum.PRICE_LOW.theValue())
        price_high=intent.getStringExtra(""+ParamEnum.PRICE_HIGH.theValue())
        rating=intent.getStringExtra(""+ParamEnum.RATING.theValue())
        tvRange.setText("SAR "+price_low+" - "+"SAR "+price_high)
    }

    override fun init() {
        filterViewModel=ViewModelProviders.of(this).get(FilterViewModel::class.java)
        filterViewModel.categoryListApi(this)
    }

    override fun initControl() {
        btnApply.setOnClickListener(this)
        btnReset.setOnClickListener(this)
    }

    override fun myObserver() {
        seekBarWithChart.onRangeChanged = { leftPinValue, rightPinValue ->
            tvRange.setText("SAR "+leftPinValue+" - "+"SAR "+rightPinValue)
            price_low=leftPinValue
            price_high=rightPinValue
        }

        filterViewModel.response.observe(this, Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })

        filterViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setDataToUI(it: CommonModel?) {
        clMain.visibility=View.VISIBLE
        honeyType.clear()
        for (i in 1..(it!!.categorylist!!.size-1)) {
           honeyType.add(it.categorylist!!.get(i).name!!)
        }
        val type=GridLayoutManager(this,2)
        type.orientation=GridLayoutManager.VERTICAL
        rvType.layoutManager=type
        rvType.adapter=FilterItemAdapter(this,honeyType,"Honey Type",this.type!!,this)
        rvType.scheduleLayoutAnimation()

        val sort=GridLayoutManager(this,2)
        type.orientation=GridLayoutManager.VERTICAL
        rvSort.layoutManager=sort
        rvSort.adapter=FilterItemAdapter(this,this.sort,"Sort By",sort_by!!,this)
        rvSort.scheduleLayoutAnimation()

        val layoutManger=LinearLayoutManager(this)
        layoutManger.orientation=LinearLayoutManager.HORIZONTAL
        rvRating.layoutManager=layoutManger
        rvRating.adapter=FilterRatingAdapter(this,ratingList,rating!!,this)
        rvRating.scheduleLayoutAnimation()
        var values=50.0f
        var barEntrys = ArrayList<BarEntry>()
        for (int in 1..100){
            barEntrys.add(BarEntry((values*int), int.toFloat()))
        }

        seekBarWithChart.setEntries(barEntrys)
        seekBarWithChart.setSelectedEntries(barEntrys)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnApply ->{
            val intent=Intent()
            intent.putExtra(""+ParamEnum.TYPE.theValue(),""+type)
            intent.putExtra(""+ParamEnum.SORT_BY.theValue(),""+sort_by)
            intent.putExtra(""+ParamEnum.PRICE_LOW.theValue(),""+price_low)
            intent.putExtra(""+ParamEnum.PRICE_HIGH.theValue(),""+price_high)
            intent.putExtra(""+ParamEnum.RATING.theValue(),""+rating)
            setResult(Activity.RESULT_OK,intent)
            finish()
            }
            R.id.btnReset ->{
            val intent=Intent()
            intent.putExtra(""+ParamEnum.TYPE.theValue(),"")
            intent.putExtra(""+ParamEnum.SORT_BY.theValue(),"")
            intent.putExtra(""+ParamEnum.PRICE_LOW.theValue(),"")
            intent.putExtra(""+ParamEnum.PRICE_HIGH.theValue(),"")
            intent.putExtra(""+ParamEnum.RATING.theValue(),"")
            setResult(Activity.RESULT_CANCELED,intent)
            finish()
            }
        }
    }

    override fun ratingClick(rating: String) {
        this.rating=rating
        rvRating.adapter!!.notifyDataSetChanged()
    }

    override fun onFilterItemClick(type: String, keyValue: String) {
        when(type)
        {
            "Honey Type" ->{
                this.type=keyValue
                rvType.adapter!!.notifyDataSetChanged()
            }
            "Sort By" -> {
                this.sort_by=keyValue
                rvSort.adapter!!.notifyDataSetChanged()
            }
        }
    }

}