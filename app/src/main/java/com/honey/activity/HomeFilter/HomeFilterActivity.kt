package com.honey.activity.HomeFilter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.honey.R
import com.honey.activity.Filter.FilterViewModel
import com.honey.activity.FilteredShops.FilteredShopActivity
import com.honey.adapter.FilterItemAdapter
import com.honey.adapter.FilterRatingAdapter
import com.honey.base.BaseActivity
import com.honey.model.request.CommonListModel
import com.honey.model.request.CommonModel
import com.honey.model.response.success.ProductDetailModel
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.TAG
import com.honey.utils.ViewExtension.observeOnce
import com.stfalcon.pricerangebar.model.BarEntry
import kotlinx.android.synthetic.main.activity_filter.*
import kotlinx.android.synthetic.main.activity_home_filter.*
import kotlinx.android.synthetic.main.activity_home_filter.btnApply
import kotlinx.android.synthetic.main.activity_home_filter.btnReset
import kotlinx.android.synthetic.main.activity_home_filter.clMain
import kotlinx.android.synthetic.main.activity_home_filter.rvRating
import kotlinx.android.synthetic.main.activity_home_filter.rvType
import kotlinx.android.synthetic.main.activity_home_filter.seekBarWithChart
import kotlinx.android.synthetic.main.activity_home_filter.tvRange
import kotlinx.android.synthetic.main.layout_billing_details.*

class HomeFilterActivity: BaseActivity(), View.OnClickListener, FilterItemAdapter.setOnFilterItemClickListner, FilterRatingAdapter.setOnItemClickListner {
    val honeyType= arrayListOf("Natural")
    val days = arrayListOf("1 Day","2 Days","3 Days","4 Days","5 Days","6 Days","7 Days","8 Days","9 Days","10 Days")
    val ratingList= arrayListOf("5","4","3","2","1")
    private lateinit var filterViewModel: HomeFilterViewModel
    var type: String?=""
    var deliveryTime: String?=""
    var sort_by: String?=""
    var rating: String?=""
    var to:String?="1"
    var from:String?="100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_filter)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"Filter")
    }

    override fun init() {
        filterViewModel= ViewModelProviders.of(this).get(HomeFilterViewModel::class.java)
        filterViewModel.categoryListApi(this)
    }

    override fun initControl() {
        btnApply.setOnClickListener(this)
        btnReset.setOnClickListener(this)
    }

    override fun myObserver() {
        seekBarWithChart.onRangeChanged = { leftPinValue, rightPinValue ->
            to=leftPinValue
            from=rightPinValue
            tvRange.setText(leftPinValue+" km"+" - "+rightPinValue+" km")
        }
        filterViewModel.response.observe(this, Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })

        filterViewModel.filterResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) fireIntent(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message)
        })
        filterViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })

    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
    }


    private fun fireIntent(it: CommonModel?) {
       Log.e(TAG(this),it!!.message!!)
       val intent=Intent(this,FilteredShopActivity::class.java)
       val list:ArrayList<ProductDetailModel> = (it.response!!.storeList as ArrayList<ProductDetailModel>?)!!
       intent.putParcelableArrayListExtra("data",list)
       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
       startActivity(intent)
    }

    private fun setDataToUI(it: CommonModel?) {
        clMain.visibility=View.VISIBLE
        honeyType.clear()
        for (i in 1..(it!!.categorylist!!.size-1)) {
            honeyType.add(it.categorylist!!.get(i).name!!)
        }
        val type= GridLayoutManager(this,2)
        type.orientation= GridLayoutManager.VERTICAL
        rvType.layoutManager=type
        rvType.adapter= FilterItemAdapter(this,honeyType,"Honey Type",this.type!!,this)
        rvType.scheduleLayoutAnimation()

        val sort= GridLayoutManager(this,3)
        type.orientation= GridLayoutManager.VERTICAL
        rvDeliveryDate.layoutManager=sort
        rvDeliveryDate.adapter= FilterItemAdapter(this,this.days,"Delivery Day",sort_by!!,this)
        rvDeliveryDate.scheduleLayoutAnimation()

        val layoutManger= LinearLayoutManager(this)
        layoutManger.orientation= LinearLayoutManager.HORIZONTAL
        rvRating.layoutManager=layoutManger
        rvRating.adapter= FilterRatingAdapter(this,ratingList,rating!!,this)
        rvRating.scheduleLayoutAnimation()
        val barEntrys = ArrayList<BarEntry>()
        val values=1.0f
        for (int in 1..100){
            barEntrys.add(BarEntry((values*int), int.toFloat()))
        }

        seekBarWithChart.setEntries(barEntrys)
        seekBarWithChart.setSelectedEntries(barEntrys)

    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.btnApply ->{
            filterViewModel.homeFilterApi(context = this,token=prefs.jwtToken!!,latitude = prefs.latitude,longitude = prefs.longitude,rating=rating!!,type=type!!,delivery_day =deliveryTime!!,to=to!!,from=from!!)
            }

            R.id.btnReset ->{
                rating=""
                type=""
                deliveryTime =""
                to="1"
                from="100"
                filterViewModel.categoryListApi(this)
            }

        }
    }


    override fun onFilterItemClick(type: String, keyValue: String) {
        Log.e(TAG(this),"Type==>"+type+" \nValue==>"+keyValue)
        when(type)
        {
            "Honey Type" ->{
                rvType.adapter!!.notifyDataSetChanged()
                this.type=keyValue
            }
            "Delivery Day" -> {
                rvDeliveryDate.adapter!!.notifyDataSetChanged()
                this.deliveryTime=keyValue.split("Day")[0].trim()
            }
        }
    }

    override fun ratingClick(rating: String) {
        this.rating=rating
        rvRating.adapter!!.notifyDataSetChanged()
    }

}