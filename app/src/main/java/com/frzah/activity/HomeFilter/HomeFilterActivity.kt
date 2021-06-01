package com.frzah.activity.HomeFilter

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.activity.FilteredShops.FilteredShopActivity
import com.frzah.adapter.FilterItemAdapter
import com.frzah.adapter.FilterRatingAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.ProductDetailModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.frzah.utils.ViewExtension.TAG
import com.stfalcon.pricerangebar.model.BarEntry
import kotlinx.android.synthetic.main.activity_home_filter.*

class HomeFilterActivity: BaseActivity(), View.OnClickListener, FilterItemAdapter.setOnFilterItemClickListner, FilterRatingAdapter.setOnItemClickListner {
    var honeyType:ArrayList<String>?=null
    var days:ArrayList<String>?=null
    val ratingList= arrayListOf("5","4","3","2","1")
    private lateinit var filterViewModel: HomeFilterViewModel
    var type: String?=""
    var deliveryTime: String?=""
    var sort_by: String?=""
    var rating: String?=""
//    var to:String?="1"
//    var from:String?="100"
    var price_low: String?="50"
    var price_high: String?="5000"
    var typeList : List<ResponseBean>?=null

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
        setToolbar(this,getString(R.string.filter))
    }

    override fun init() {
        honeyType=arrayListOf(getString(R.string.natural))
        days=arrayListOf(getString(com.frzah.R.string.one_daya),getString(R.string.two_days),getString(R.string.three_days),getString(R.string.four_days),getString(R.string.five_days),getString(R.string.six_day),getString(R.string.seven_days),getString(R.string.eight_days),getString(R.string.nine_days),getString(R.string.ten_days))
        filterViewModel= ViewModelProviders.of(this).get(HomeFilterViewModel::class.java)
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

        filterViewModel.filterResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) fireIntent(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message)
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
        honeyType!!.clear()
        typeList=it?.categorylist
        for (i in 1..(it!!.categorylist!!.size-1)) {
            honeyType!!.add(it.categorylist!!.get(i).name!!)
        }
        val type= GridLayoutManager(this,2)
        type.orientation= GridLayoutManager.VERTICAL
        rvType.layoutManager=type
        rvType.adapter= FilterItemAdapter(this,honeyType!!,"Honey Type",this.type!!,this)
        rvType.scheduleLayoutAnimation()

        val sort= GridLayoutManager(this,3)
        type.orientation= GridLayoutManager.VERTICAL
        rvDeliveryDate.layoutManager=sort
        rvDeliveryDate.adapter= FilterItemAdapter(this,this.days!!,"Delivery Day",sort_by!!,this)
        rvDeliveryDate.scheduleLayoutAnimation()

        val layoutManger= LinearLayoutManager(this)
        layoutManger.orientation= LinearLayoutManager.HORIZONTAL
        rvRating.layoutManager=layoutManger
        rvRating.adapter= FilterRatingAdapter(this,ratingList,rating!!,this)
        rvRating.scheduleLayoutAnimation()
        var values=50.0f
        var barEntrys = ArrayList<BarEntry>()
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
            filterViewModel.homeFilterApi(context = this,token=prefs.jwtToken!!,latitude = prefs.latitude,longitude = prefs.longitude,rating=rating!!,type=getHoneyType(type)!!,delivery_day =deliveryTime!!,price_low=price_low!!,price_high=price_high!!)
            }

            R.id.btnReset ->{
                rating=""
                type=""
                deliveryTime =""
                price_low="50"
                price_high="5000"
                filterViewModel.categoryListApi(this)
            }

        }
    }
    override fun onFilterItemClick(type: String, keyValue: String,days:Int) {
        Log.e(TAG(this),"Type==>"+type+" \nValue==>"+keyValue)
        when(type)
        {
            "Honey Type" ->{
                rvType.adapter!!.notifyDataSetChanged()
                this.type=keyValue
            }
            "Delivery Day" -> {
                rvDeliveryDate.adapter!!.notifyDataSetChanged()
                this.deliveryTime=""+days
            }
        }
    }
    override fun ratingClick(rating: String) {
        this.rating=rating
        rvRating.adapter!!.notifyDataSetChanged()
    }

    private fun getHoneyType(type: String?): String? {
        var id=""
        for(i in typeList?.indices!!){
            if(typeList?.get(i)?.name.equals(type,ignoreCase = true)){
                id= typeList?.get(i)?.id.toString() ?:"0"
                break
            }
        }

        return id

    }

}