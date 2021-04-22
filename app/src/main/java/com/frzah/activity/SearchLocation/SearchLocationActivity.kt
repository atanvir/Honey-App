package com.frzah.activity.SearchLocation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.activity.AddNewAddress.AddNewAddressActivity
import com.frzah.activity.DeliveryAddress.DeliveryAddressActivity
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.SelectLocation.SelectLocationActivity
import com.frzah.adapter.AddressAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.startActivity
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_search_location.*

class SearchLocationActivity : BaseActivity(), View.OnClickListener, AddressAdapter.setOnAddressClickListner {
    private lateinit var searchViewModel: SearchViewModel
    private var addressList:MutableList<ResponseBean>?=null
    var pos:Int=-1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_location)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this,getString(R.string.search_location))
    }

    override fun init() {
        searchViewModel= ViewModelProviders.of(this).get(SearchViewModel::class.java)
        searchViewModel.userAddressApi(this,prefs.jwtToken!!)
        PushDownAnim.setPushDownAnimTo(tvAddress).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        PushDownAnim.setPushDownAnimTo(tvCurrentLocation).setScale(PushDownAnim.MODE_SCALE, 0.89f)
    }

    override fun initControl() {
        tvAddress.setOnClickListener(this)
        tvCurrentLocation.setOnClickListener(this)
    }

    override fun myObserver() {

        searchViewModel.response.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it.user_address!!)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })

        searchViewModel.removeAddressResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) removeData(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })

        searchViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun removeData(it: CommonModel?) {
        addressList!!.removeAt(pos)
        if(addressList!!.size>0) tvAddressLabel.visibility=View.VISIBLE
        else tvAddressLabel.visibility=View.GONE
        rvAddress.adapter!!.notifyDataSetChanged()
    }

    private fun setDataToUi(response: MutableList<ResponseBean>) {
        if(response.size>0){
            tvAddressLabel.visibility=View.VISIBLE
        }
        addressList=response
        rvAddress.layoutManager=LinearLayoutManager(this)
        rvAddress.adapter=AddressAdapter(this,addressList!!,this)
        rvAddress.scheduleLayoutAnimation()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.tvAddress ->{ startActivity(this, AddNewAddressActivity::class.java) }
            R.id.tvCurrentLocation ->{
                val intent = Intent(this, SelectLocationActivity::class.java)
                intent.putExtra("cameFrom",SearchLocationActivity::class.simpleName)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent) }
            R.id.edFind ->{
                val intent = Intent(this, SelectLocationActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("isFromSearch",true)
                startActivity(intent) }
        }
    }

    override fun onRemove(pos: Int, address_id: String) {
        this.pos=pos
        searchViewModel.removeAddressApi(this,address_id,prefs.jwtToken!!)
    }

    override fun onBackPressed() {
        if(prefs.cameFrom.equals(MainActivity::class.simpleName))
        {
            finish()
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }else{
            finish()
            val intent=Intent(this,DeliveryAddressActivity::class.java)
            startActivity(intent)
        }
    }





}