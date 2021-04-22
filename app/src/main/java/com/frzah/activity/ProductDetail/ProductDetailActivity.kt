package com.frzah.activity.ProductDetail

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.Review.ReviewActivity
import com.frzah.adapter.SpecificationAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.GuestDataModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.GuestData
import com.frzah.utils.ParamEnum
import com.frzah.utils.ViewExtension.readMore
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_product_detail.*

class ProductDetailActivity : BaseActivity(), View.OnClickListener {
    private lateinit var productViewModel: ProductDetailViewModel
    private var quantity:Int?=null
    private var stockQuantity:Long?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        init()
        initControl()
        myObserver()
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this, "")
    }
    override fun init() {
        PushDownAnim.setPushDownAnimTo(tvReview).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        tvMRP.paintFlags=tvMRP.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        productViewModel = ViewModelProvider(this).get(ProductDetailViewModel::class.java)
        productViewModel.productDetailApi(this, "" + intent.getStringExtra("product_id"), prefs.jwtToken!!)
    }
    override fun initControl() {
        tvReview.setOnClickListener(this)
        ivPlus.setOnClickListener(this)
        ivMinus.setOnClickListener(this)
        btnAddtoCart.setOnClickListener(this)
    }
    override fun myObserver() {
        productViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it.response)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })

        productViewModel.removeCartResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) responseHandler()
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })

        productViewModel.updateCartResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) responseHandler()
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })

        productViewModel.addCartResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) responseHandler()
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showAlertDialog()
        })

        productViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })
    }
    private fun responseHandler() {
        tvQuality.text=""+quantity

        if(quantity==0){
            btnAddtoCart.text=getString(R.string.add_to_cart)
            ivCart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bitmap_cart_bag))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnAddtoCart.backgroundTintList=getColorStateList(R.color.app_theme_organe)
            }
        }else{
            btnAddtoCart.text=getString(R.string.view_cart)
            ivCart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bitmap_cart_bag_green))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnAddtoCart.backgroundTintList=getColorStateList(R.color.green)
            }
        }
    }
    private fun showAlertDialog() {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_aleart)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val tvYes = dialog.findViewById<TextView>(R.id.tvYes)
        tvYes.setOnClickListener {
            dialog.dismiss()
            if(prefs.jwtToken!!.equals("")) {
                GuestData.instance!!.removeAllData()
                GuestData.instance!!.addData(GuestDataModel(intent.getStringExtra("product_id")!!, intent.getStringExtra("seller_id")!!, 1))
                productViewModel.productDetailApi(this, "" + intent.getStringExtra("product_id"), prefs.jwtToken!!)
            }
            else {productViewModel.addToCartApi(this, intent.getStringExtra("product_id")!!, intent.getStringExtra("seller_id")!!, "yes", prefs.jwtToken!!, 1) }
        }

        val tvNo = dialog.findViewById<TextView>(R.id.tvNo)
        tvNo.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }
    private fun setDataToUi(response: ResponseBean?) {
        btnAddtoCart.visibility=View.VISIBLE
        ivCart.visibility=View.VISIBLE
        if(prefs.jwtToken!!.equals("")){
         for(i in 0..(GuestData.instance!!.allData!!.size-1)){
             if(GuestData.instance!!.allData!!.get(i).product_id.equals(intent.getStringExtra("product_id")))
             {
                 response!!.havecart="yes"
                 break
             }
         }
        }

        setRoundImage(this, ivProductPic, lvProductDetail, response!!.images!!)
        tvName.text=response.name
        tvRating.text = "" + response.rating
        if(response.review!!.size==0) tvReviews.text="(+ 0 "+getString(R.string.review)+")"
        else tvReviews.text="(+"+response.review!!.size+" "+getString(R.string.review)+")"
        if(prefs?.selectedLanguage.equals("en")) tvSellingPrice.text =getString(R.string.sar)+" "+response.sp
        else tvSellingPrice.text =response.sp+" "+getString(R.string.sar)
        if(response.mrp.equals("0")){ tvMRP.visibility=View.GONE }
        tvMRP.text =getString(R.string.sar)+" "+response.mrp
        tvDesc.text=response.description
        tvSize.text=response.size
        readMore(tvDesc)
        stockQuantity=response.quantity

        if(response.quantity!!<1){
            btnAddtoCart.text=getString(R.string.out_of_stock)
            ivCart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bitmap_cart_bag_red))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnAddtoCart.backgroundTintList=getColorStateList(R.color.red)
            }
        }else if(response.havecart.equals("yes") )
        {
            btnAddtoCart.text=getString(R.string.view_cart)
            ivCart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bitmap_cart_bag_green))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnAddtoCart.backgroundTintList=getColorStateList(R.color.green)
            }
        }else
        {
            btnAddtoCart.text=getString(R.string.add_to_cart)
            ivCart.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.bitmap_cart_bag))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnAddtoCart.backgroundTintList=getColorStateList(R.color.app_theme_organe)
            }

        }
        if(prefs.jwtToken!!.equals("")){
            quantity=GuestData.instance!!.getQuantity("" + intent.getStringExtra("product_id"))
        }else{
           quantity=response.haveQTY
         }
        tvQuality.text=""+quantity
        if(response!!.attributes!!.size>0)
        {
            if(response.attributes!!.get(0).image.equals("")  && response.attributes!!.get(0).labal==null) tvLabelSpecfication.visibility=View.GONE
            else if(response.attributes!!.get(0).image==null  && response.attributes!!.get(0).labal.equals("")) tvLabelSpecfication.visibility=View.GONE
            else tvLabelSpecfication.visibility=View.VISIBLE

        }else{
            tvLabelSpecfication.visibility=View.GONE
        }
        rvSpecification.layoutManager=LinearLayoutManager(this)
        rvSpecification.adapter=SpecificationAdapter(this, response!!.attributes)
        rvSpecification.scheduleLayoutAnimation()
    }
    override fun onClick(p0: View?) {
     when(p0!!.id) {
         R.id.tvReview -> {
             val intent = Intent(this, ReviewActivity::class.java)
             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
             intent.putExtra("type", ParamEnum.PRODUCT_TYPE.theValue() as String)
             intent.putExtra("id", getIntent().getStringExtra("product_id")!!)
             startActivity(intent)
         }
         R.id.ivMinus -> {
             if (prefs.jwtToken.equals("")) {
                 quantity = quantity!! - 1
                 if (quantity == 0) GuestData.instance!!.removeData(intent.getStringExtra("product_id")!!)
                 else GuestData.instance!!.updateQuantity(quantity!!.toLong(), intent.getStringExtra("product_id")!!)
                 productViewModel.productDetailApi(this, "" + intent.getStringExtra("product_id"), prefs.jwtToken!!)

             } else {
                 quantity = quantity!! - 1
                 if (quantity == 0) productViewModel.removeToCartApi(this, intent.getStringExtra("product_id")!!, prefs.jwtToken!!, quantity!!.toLong())
                 else productViewModel.updateCartApi(this, intent.getStringExtra("product_id")!!, prefs.jwtToken!!, quantity!!)
             }
         }
         R.id.ivPlus -> {
             if (checkAvaialbleStock()) {
                 if (prefs.jwtToken!!.equals("")) {
                     quantity = quantity!! + 1
                     if (quantity == 1) {
                         if (checkStore(intent.getStringExtra("seller_id")!!)) {
                             GuestData.instance!!.addData(GuestDataModel(intent.getStringExtra("product_id")!!, intent.getStringExtra("seller_id")!!, quantity!!.toLong()))
                             productViewModel.productDetailApi(this, "" + intent.getStringExtra("product_id"), prefs.jwtToken!!)
                         } else {
                             showAlertDialog()
                         }
                     } else {
                         GuestData.instance!!.updateQuantity(quantity!!.toLong(), intent.getStringExtra("product_id")!!)
                         productViewModel.productDetailApi(this, "" + intent.getStringExtra("product_id"), prefs.jwtToken!!)
                     }
                 } else {
                     quantity = quantity!! + 1
                     if (quantity == 1) productViewModel.addToCartApi(this, intent.getStringExtra("product_id")!!, intent.getStringExtra("seller_id")!!, "no", prefs.jwtToken!!, quantity!!.toLong())
                     else productViewModel.updateCartApi(this, intent.getStringExtra("product_id")!!, prefs.jwtToken!!, quantity!!)
                 }
             } else {
                 showSnackBar(this, getString(R.string.out_of_stock))
             }
         }
         R.id.btnAddtoCart -> {
             if(checkText()) {
             if (checkAvaialbleStock()) {
                     if (prefs.jwtToken!!.equals("")) {
                         quantity = quantity!! + 1
                         if (checkStore(intent.getStringExtra("seller_id")!!)) {
                             GuestData.instance!!.addData(GuestDataModel(intent.getStringExtra("product_id")!!, intent.getStringExtra("seller_id")!!, quantity!!.toLong()))
                             productViewModel.productDetailApi(this, "" + intent.getStringExtra("product_id"), prefs.jwtToken!!)
                         } else {
                             showAlertDialog()
                         }
                     } else {
                         quantity = quantity!! + 1
                         productViewModel.addToCartApi(this, intent.getStringExtra("product_id")!!, intent.getStringExtra("seller_id")!!, "no", prefs.jwtToken!!, quantity!!.toLong())
                     }
                 }
             }
         }
     }
    }
    private fun checkText(): Boolean {
        var ret=true
        if(btnAddtoCart.text.toString().equals(getString(R.string.view_cart))){
            ret=false
            val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("cameFrom",ProductDetailActivity::class.simpleName)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        return ret
    }
    private fun checkAvaialbleStock(): Boolean {
        var ret=true
        if(!(stockQuantity!! >= (quantity!! + 1).toLong())) {
            ret=false
            showSnackBar(this, getString(R.string.out_of_stock))
        }
     return ret
    }
    private fun checkStore(sellerId: String): Boolean {
        var ret=true
        if(GuestData.instance!!.allData!!.size>0){
            for(i in 0..(GuestData.instance!!.allData!!.size-1)){
                if(!(GuestData.instance!!.allData!!.get(i).store_id.equals(sellerId))){
                    ret=false
                    break
                }
            }
        }
        return ret
    }
}