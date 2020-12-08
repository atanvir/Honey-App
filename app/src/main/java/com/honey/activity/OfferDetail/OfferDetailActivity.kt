package com.honey.activity.OfferDetail

import android.graphics.Paint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.load.engine.executor.GlideExecutor.UncaughtThrowableStrategy.LOG
import com.honey.R
import com.honey.adapter.OfferProductAdapter
import com.honey.base.BaseActivity
import com.honey.model.response.success.ResponseBean
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension
import kotlinx.android.synthetic.main.activity_offer_detail.*
import kotlinx.android.synthetic.main.adapter_common_product.view.*

class OfferDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var offerDetailViewModel: OfferDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_offer_detail)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"")
    }

    override fun init() {
        tvMRP.paintFlags=tvMRP.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        offerDetailViewModel=ViewModelProviders.of(this).get(OfferDetailViewModel::class.java)
        offerDetailViewModel.offerDetailApi(this,prefs.jwtToken!!,intent.getStringExtra("offer_id")!!)
    }

    override fun initControl() {
        btnAddtoCart.setOnClickListener(this)
    }

    override fun myObserver() {
        offerDetailViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it.offer_detail)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message)
        })
        offerDetailViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setDataToUI(response: ResponseBean?) {

        /* --Offer Details-- */
        CommonUtils.setNormalImage(this,ivOfferPic,lvCoverOffer,response!!.images)
        tvName.text=response.name
        tvSellingPrice.text="SAR "+response.mrp
        tvMRP.text="SAR "+response.offer_price
        tvDesc.text=response.description

        /* --Seller Deatils--*/
        CommonUtils.setRoundImage(this,ivSeller,null,response.seller_image!!)
        tvSellerName.text=response.seller_name
        tvSellerAddress.text=response.seller_address

        /* --Offered Products--*/
        rvOffers.layoutManager=LinearLayoutManager(this)
        rvOffers.adapter=OfferProductAdapter(this,response.productList!!)
        rvOffers.scheduleLayoutAnimation()

        /* --Add To Cart Button--*/
        if(response.havecart.equals("no",ignoreCase = true)){
        btnAddtoCart.visibility=View.VISIBLE
        ivCart.visibility=View.VISIBLE

        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnAddtoCart ->{

            }
        }
    }

}