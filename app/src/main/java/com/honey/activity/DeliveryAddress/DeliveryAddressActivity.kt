package com.honey.activity.DeliveryAddress

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.honey.R
import com.honey.activity.Main.MainActivity
import com.honey.activity.SearchLocation.SearchLocationActivity
import com.honey.activity.SearchLocation.SearchViewModel
import com.honey.adapter.AddressAdapter
import com.honey.base.BaseActivity
import com.honey.model.request.CommonModel
import com.honey.model.response.success.ResponseBean
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.observeOnce
import kotlinx.android.synthetic.main.activity_delivery_address.*
import kotlinx.android.synthetic.main.activity_search_location.*

class DeliveryAddressActivity : BaseActivity(), View.OnClickListener, AddressAdapter.setOnAddressClickListner {
    private var addressList:MutableList<ResponseBean>?=null
    var pos:Int=-1
    private lateinit var deliveryViewModel:DeliveryAddressViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_address)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"Delivery Address")
    }


    override fun init() {
        deliveryViewModel=ViewModelProviders.of(this).get(DeliveryAddressViewModel::class.java)
        deliveryViewModel.userAddressApi(this,prefs.jwtToken!!)
    }

    override fun initControl() {
        btnAddnewAddress.setOnClickListener(this)
    }

    override fun myObserver() {

        deliveryViewModel.response.observe(this, Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })

        deliveryViewModel.removeAddressResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) removeData(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })

        deliveryViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })

    }

    private fun removeData(it: CommonModel?) {
        addressList!!.removeAt(pos)
        recyclerView.adapter!!.notifyDataSetChanged()
    }


    private fun setDataToUI(it: CommonModel?) {
        btnAddnewAddress.visibility=View.VISIBLE
        addressList=it!!.user_address!!
        recyclerView.layoutManager=LinearLayoutManager(this)
        recyclerView.adapter=AddressAdapter(this,addressList!!,this)
        recyclerView.scheduleLayoutAnimation()
    }

    override fun onClick(p0: View?) {
        when(p0!!.id) {

            R.id.btnAddnewAddress -> {
                prefs.cameFrom = DeliveryAddressActivity::class.simpleName!!
                finish()
                val intent = Intent(this, SearchLocationActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onRemove(pos: Int, address_id: String) {
        this.pos=pos
        deliveryViewModel.removeAddressApi(this,address_id,prefs.jwtToken!!)
    }

}