package com.frzah.activity.Filter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.adapter.FilterItemAdapter
import com.frzah.adapter.FilterRatingAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.stfalcon.pricerangebar.model.BarEntry
import kotlinx.android.synthetic.main.activity_filter.*

class FilterActivity : BaseActivity(), View.OnClickListener, FilterRatingAdapter.setOnItemClickListner, FilterItemAdapter.setOnFilterItemClickListner {
    var honeyType:ArrayList<String>?=null
    var sort:ArrayList<String>?=null
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
        setToolbar(this,getString(R.string.filter))
        type=intent.getStringExtra(""+ParamEnum.TYPE.theValue())
        sort_by=intent.getStringExtra(""+ParamEnum.SORT_BY.theValue())
        price_low=intent.getStringExtra(""+ParamEnum.PRICE_LOW.theValue())
        price_high=intent.getStringExtra(""+ParamEnum.PRICE_HIGH.theValue())
        rating=intent.getStringExtra(""+ParamEnum.RATING.theValue())
        tvRange.setText(getString(R.string.sar)+" "+ price_low+" - "+getString(R.string.sar)+" "+ price_high)
    }

    override fun init() {
        honeyType=arrayListOf(getString(R.string.natural),getString(R.string.arabian),getString(R.string.ksa),getString(R.string.dark),getString(R.string.acacia),getString(R.string.black_forest))
        sort=arrayListOf(getString(R.string.popular),getString(R.string.free_delivery),getString(R.string.nearest),getString(R.string.delivery_time),getString(R.string.price_range))
        filterViewModel=ViewModelProviders.of(this).get(FilterViewModel::class.java)
        filterViewModel.categoryListApi(this)
    }

    override fun initControl() {
        btnApply.setOnClickListener(this)
        btnReset.setOnClickListener(this)
    }

    override fun myObserver() {
        seekBarWithChart.onRangeChanged = { leftPinValue, rightPinValue ->
            tvRange.setText(getString(R.string.sar)+" "+ leftPinValue+" - "+getString(R.string.sar)+ " "+rightPinValue)
            price_low=leftPinValue
            price_high=rightPinValue
        }

        filterViewModel.response.observe(this, Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })

        filterViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setDataToUI(it: CommonModel?) {
        clMain.visibility=View.VISIBLE
        honeyType!!.clear()
        for (i in 1..(it!!.categorylist!!.size-1)) {
           honeyType!!.add(it.categorylist!!.get(i).name!!)
        }
        val type=GridLayoutManager(this,2)
        type.orientation=GridLayoutManager.VERTICAL
        rvType.layoutManager=type
        rvType.adapter=FilterItemAdapter(this,honeyType!!,"Honey Type",this.type!!,this)
        rvType.scheduleLayoutAnimation()

        val sort=GridLayoutManager(this,2)
        type.orientation=GridLayoutManager.VERTICAL
        rvSort.layoutManager=sort
        rvSort.adapter=FilterItemAdapter(this,this.sort!!,"Sort By",sort_by!!,this)
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

    override fun onFilterItemClick(type: String, keyValue: String,days:Int) {
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