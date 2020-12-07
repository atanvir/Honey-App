package com.honey.activity.OrderDetail

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.honey.R
import com.honey.activity.Main.MainActivity
import com.honey.activity.Order.OrderActivity
import com.honey.activity.Rating.RatingActivity
import com.honey.adapter.BagItemAdapter
import com.honey.adapter.OrderStatusAdapter
import com.honey.base.BaseActivity
import com.honey.model.response.success.OrderModel
import com.honey.model.response.success.ResponseBean
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import kotlinx.android.synthetic.main.activity_order_detail.*
import kotlinx.android.synthetic.main.activity_order_detail.rvOrdersStatus
import kotlinx.android.synthetic.main.layout_billing_details.*

class OrderDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var orderDetailViewModel: OrderDetailViewModel
    var orderStatus= arrayListOf("Pending","Confirmed","Ready to ship","Out for delivery","Delivered","Cancelled")
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
        CommonUtils.setToolbar(this,"Order Details")
    }

    override fun init() {
        if(intent.getStringExtra("cameFrom").equals("upcoming")){
            orderStatus.clear()
            orderStatus=arrayListOf("Pending","Processed","Shipped")
        }else{
            orderStatus.clear()
            orderStatus=arrayListOf("Delivered","Cancelled")
            btnCancel.visibility=View.GONE
            tvTrackingId.visibility=View.GONE
            tvTrackingIdLabel.visibility=View.GONE
            tvTrackingUrl.visibility=View.GONE
            tvTrackingUrlLabel.visibility=View.GONE
        }
        btnRate.visibility=View.GONE
        btnReOrder.visibility=View.GONE
        orderDetailViewModel= ViewModelProviders.of(this).get(OrderDetailViewModel::class.java);
        orderDetailViewModel.orderDetail(this,prefs.jwtToken!!,intent.getStringExtra(ParamEnum.ORDER_ID.theValue() as String)!!)
    }

    override fun initControl() {
        btnRate.setOnClickListener(this)
        btnReOrder.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
    }

    override fun myObserver() {
        orderDetailViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it.response)
            else if (it.status!!.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this, it.message)
        })

        orderDetailViewModel.cancelOrderResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) {
            CommonUtils.startActivity(this,OrderActivity::class.java)
            Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }

            else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })


        orderDetailViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setDataToUi(data: ResponseBean?) {
        tvOrderId.text="Order Id : #"+data!!.order_number
        tvDeliveryStatus.text=data.status
        tvTrackingId.text=data.tracking_id
        tvTrackingUrl.text=data.tracking_url
        tvOrderOnDate.text=data.dispatch_at
        tvDeliverOnDate.text=data.order_date

        // Order Summary
        for(i in 0..orderStatus.size-1){
            if(orderStatus.get(i).equals("Processed").and(data.status.equals("Confirmed")))  pos=i
            else if(orderStatus.get(i).equals("Shipped").and(data.status.equals("Ready to ship")))  pos=i
            else if(data.status.equals(orderStatus.get(i))) pos=i
            listStatus.add(OrderModel(orderStatus.get(i),false))
        }

        for(i in 0..pos)
        {
            listStatus.get(i).isChecked=true
        }

        rvOrdersStatus.layoutManager=LinearLayoutManager(this)
        rvOrdersStatus.adapter= OrderStatusAdapter(this,listStatus)
        rvOrdersStatus.scheduleLayoutAnimation()

        // Seller Details
        CommonUtils.setRoundImage(this,ivSeller,null,data.images!!)
        tvSellerName.text=data.seller_name
        tvSellerAddress.text=data.seller_address

        // Order Items
        rvItems.layoutManager=LinearLayoutManager(this)
        rvItems.adapter= BagItemAdapter(this,data.orderItems!!,null)
        rvItems.scheduleLayoutAnimation()

        // Delivery To
        tvDeliveryTo.text="Delivery to : "+data.customerName
        tvDeliveryAddress.text="Address : "+data.customerAddress+"\nMobile No : "+data.customerPhone

        // Payment Details
        if(data.tax!!.equals("0")) clTextFees.visibility=View.GONE
        if(data.total_discount!!.equals("0")) clDiscount.visibility=View.GONE
        if(data.shipping_charge.equals("0")) clShippingCharges.visibility=View.GONE
        tvSubTotal.text=data.totalAmount
        tvTax.text=""+data.tax
        tvDiscount.text=""+data.total_discount
        tvShippingCharges.text=data.shipping_charge
        tvTotal.text=data.finalAmount
        tvPaymentMethod.text=if(data.payment_type.equals("COD",ignoreCase = true)) "Cash On Delivery" else "Online"

    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnReOrder ->{
                val intent=Intent(this, MainActivity::class.java)
                intent.putExtra("cameFrom","Re-Order")
                startActivity(intent)
            }

            R.id.btnRate ->{ CommonUtils.startActivity(this, RatingActivity::class.java) }
            R.id.btnCancel ->{ orderDetailViewModel.cancelOrderApi(this,prefs.jwtToken!!,intent.getStringExtra(ParamEnum.ORDER_ID.theValue() as String)!!) }

        }
    }

}