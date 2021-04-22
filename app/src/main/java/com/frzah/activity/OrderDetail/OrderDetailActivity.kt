package com.frzah.activity.OrderDetail

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.Order.OrderActivity
import com.frzah.activity.Rating.RatingActivity
import com.frzah.adapter.BagItemAdapter
import com.frzah.adapter.OrderStatusAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.response.success.OrderModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.startActivity
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.layout_billing_details.*

class OrderDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var orderDetailViewModel: OrderDetailViewModel
    var orderStatus: ArrayList<String>?=null
    val listStatus=ArrayList<OrderModel>()
    var pos:Int=-1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this, getString(R.string.order_details))
    }

    override fun init() {
        orderStatus= arrayListOf(getString(R.string.pending))
        if(intent.getStringExtra("cameFrom").equals("upcoming")){
            orderStatus!!.clear()
            orderStatus=arrayListOf(getString(R.string.pending), getString(R.string.processed), getString(R.string.shipped),getString(R.string.deliveried))
        }else{
            orderStatus!!.clear()
            orderStatus=arrayListOf("")
            btnCancel.visibility=View.GONE
            tvTrackingId.visibility=View.VISIBLE
            tvTrackingIdLabel.visibility=View.VISIBLE
            tvTrackingUrl.visibility=View.VISIBLE
            tvTrackingUrlLabel.visibility=View.VISIBLE
        }

        btnRate.visibility=View.GONE
        btnReOrder.visibility=View.GONE
        orderDetailViewModel= ViewModelProviders.of(this).get(OrderDetailViewModel::class.java);
        orderDetailViewModel.orderDetail(this, prefs.jwtToken!!, intent.getStringExtra(ParamEnum.ORDER_ID.theValue() as String)!!)
    }

    override fun initControl() {
        btnRate.setOnClickListener(this)
        btnReOrder.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        tvTrackingUrl.setOnClickListener(this)
    }

    override fun myObserver() {
        orderDetailViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it.response)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })

        orderDetailViewModel.cancelOrderResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) {
                startActivity(Intent(this,OrderActivity::class.java).putExtra("body","Your order has been cancelled"))
                finish()
            } else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })

        orderDetailViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })

    }

    private fun setDataToUi(data: ResponseBean?) {
        if(intent.getStringExtra("cameFrom").equals("upcoming") || checkUpcoming(data!!.status)){
            orderStatus!!.clear()
            orderStatus=arrayListOf(getString(R.string.pending), getString(R.string.processed),getString(R.string.shipped),getString(R.string.deliveried))
            btnCancel.visibility=View.VISIBLE
        }else{

            orderStatus!!.clear()
            orderStatus=arrayListOf(""+data!!.status)
            if(data.status.equals(getString(R.string.cancelled))){
                tvTrackingId.visibility=View.GONE
                tvTrackingIdLabel.visibility=View.GONE
                tvTrackingUrl.visibility=View.GONE
                tvTrackingUrlLabel.visibility=View.GONE
            }
        }

        tvOrderId.text=getString(R.string.order_id)+data!!.order_number
        tvDeliveryStatus.text=data.status
        if(data.status.equals(getString(R.string.deliveried))) { tvLabelDelivered.text=getString(R.string.delivered_on) }
        else if(data.status.equals(getString(R.string.cancelled))) {
            tvLabelDelivered.visibility=View.GONE
            tvDeliverOnDate.visibility=View.GONE
        }
        tvTrackingId.text=data.tracking_id
        tvTrackingUrl.tag=data.tracking_url
        tvOrderOnDate.text=data.order_date
        tvDeliverOnDate.text=data.dispatch_at

        // Order Summary
        for(i in 0..orderStatus!!.size-1){
            if(data.status.equals("pending",ignoreCase = true)) pos=0
            else if(data.status.equals("processing",ignoreCase = true)) pos=1
            else if(data.status.equals("Ready to ship",ignoreCase = true) || data.status.equals("shipped",ignoreCase = true)) pos=2
            else pos=i
            listStatus.add(OrderModel(orderStatus!!.get(i), false))
        }


        for(i in 0..pos)
        {
          listStatus.get(i).isChecked=true
        }

        rvOrdersStatus.layoutManager=LinearLayoutManager(this)
        rvOrdersStatus.adapter= OrderStatusAdapter(this, listStatus)
        rvOrdersStatus.scheduleLayoutAnimation()

        // Seller Details
        setRoundImage(this, ivSeller, null, data.images!!)
        tvSellerName.text=data.seller_name
        tvSellerAddress.text=data.seller_address

        // Order Items
        rvItems.layoutManager=LinearLayoutManager(this)
        rvItems.adapter= BagItemAdapter(this, data.orderItems!!, null)
        rvItems.scheduleLayoutAnimation()

        // Delivery To
        tvDeliveryTo.text=getString(R.string.delivery_to)+" "+data.customerName
        tvDeliveryAddress.text=getString(R.string.address_)+data.customerAddress+getString(R.string.mobile_numbers)+" "+data.customerPhone

        // Payment Details
        if(data.tax!!.equals("0")) clTextFees.visibility=View.GONE
        if(data.total_discount!!.equals("0")) clDiscount.visibility=View.GONE
        if(data.shipping_charge.equals("0")) clShippingCharges.visibility=View.GONE
        tvSubTotal.text=data.totalAmount
        tvTax.text=""+data.tax
        tvDiscount.text=""+data.total_discount
        tvShippingCharges.text=data.shipping_charge
        tvTotal.text=data.finalAmount
        tvPaymentMethod.text=if(data.payment_type.equals("COD", ignoreCase = true)) getString(R.string.cod) else getString(
            R.string.online)
    }

    private fun checkUpcoming(status: String?): Boolean {
        when(status!!.toLowerCase()){
            "cancelled" -> return false
            "delivered" -> return false
            else -> return true
        }



    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnReOrder -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("cameFrom", "Re-Order")
                startActivity(intent)
            }
            R.id.tvTrackingUrl -> {
                if(tvTrackingUrl.tag.toString().equals("")){
                    showSnackBar(this,getString(R.string.tracking_url_not_found))
                }else {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(tvTrackingUrl.tag.toString())
                        startActivity(intent)
                    }catch (e:Exception){
                        showSnackBar(this,getString(R.string.invalid_url))
                    }
                }
            }
            R.id.btnRate -> { startActivity(this, RatingActivity::class.java) }
            R.id.btnCancel -> { orderDetailViewModel.cancelOrderApi(this, prefs.jwtToken!!, intent.getStringExtra(ParamEnum.ORDER_ID.theValue() as String)!!) }

        }
    }

}