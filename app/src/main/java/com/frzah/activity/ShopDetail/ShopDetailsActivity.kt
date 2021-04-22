package com.frzah.activity.ShopDetail

import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.activity.Login.LoginActivity
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.Review.ReviewActivity
import com.frzah.adapter.HomeOptionAdapter
import com.frzah.adapter.ProductAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.CartDetailsModel
import com.frzah.model.response.success.CommonProductItemModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.*
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.startActivity
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_shop_details.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*
import java.util.*

class ShopDetailsActivity : BaseActivity(), View.OnClickListener, HomeOptionAdapter.onOptionClickListner, ProductAdapter.onProductClickListner {
    private lateinit var shopViewModel: ShopDetailsViewModel
    private var categoryList:List<CommonProductItemModel>?=null
    private var cartDetailModel:CartDetailsModel?=null
    private var product_id: String?=null
    private var seller_id: String?=null
    private var pos: Int?=null
    private var haveCart=false
    private var cart_id:String=""
    private var quantity:String=""
    var count=0
    var totalPrice=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_details)
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
//        val height: Int = Resources.getSystem().getDisplayMetrics().heightPixels
//        Log.d("test"," Device Height "+height)
//        Log.d("test"," Device Height "+rvOptions.height)
//        var param  = rvShops.layoutParams
//        param.height=height-rvOptions.height -25
//        rvShops.layoutParams=param
        guestData()
        shopViewModel = ViewModelProvider(this).get(ShopDetailsViewModel::class.java)
        shopViewModel.storeDetailApi(this, "" + intent.getStringExtra("store_id"), prefs.jwtToken!!, cart_id, quantity)
    }

    private fun guestData() {
        if(prefs.jwtToken!!.equals(""))
        {
            cart_id= CommonUtils.getGuestData("" + ParamEnum.PRODUCT_ID.theValue())
            seller_id= CommonUtils.getGuestData("" + ParamEnum.SELLER_ID.theValue())
            quantity= CommonUtils.getGuestData("" + ParamEnum.QUANTITY.theValue())
        }
    }

    override fun initControl() {
        tvReview.setOnClickListener(this)
        ivFav.setOnClickListener(this)
        btnAddtoCart.setOnClickListener(this)
        PushDownAnim.setPushDownAnimTo(tvReview).setScale(PushDownAnim.MODE_SCALE, 0.89f)
    }
    override fun myObserver(){
        shopViewModel.response.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it.response)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })

        shopViewModel.onProductResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) setProductByCategory(it.response)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })

        shopViewModel.onCartResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkData(it)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showAlertDialog()
        })

        shopViewModel.onFavResponse.observe(this, Observer {
            if (it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkFavData(it)
            else if (it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this, it.message)
        })
        shopViewModel.error.observe(this, Observer { ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun checkFavData(response: CommonModel?) {
        if(response!!.message.equals(getString(R.string.product_removed_from_wishlist)))
        {
            categoryList!!.get(pos!!).favourite = "no"
            rvShops.adapter!!.notifyItemChanged(pos!!)
        }else if(response.message.equals(getString(R.string.product_added_to_wishlist))){
            categoryList!!.get(pos!!).favourite = "yes"
            rvShops.adapter!!.notifyItemChanged(pos!!)
        }else if(response.message.equals(getString(R.string.store_added_to_wishlist)))
        {
            ivHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bitmap_fav_in))
        }
        else {
            ivHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bitmap_fav_out))
        }
    }
    private fun checkData(response: CommonModel?) {
        if(response!!.message.equals(getString(R.string.product_added_to_cart))) {
            categoryList!!.get(pos!!).havecart = "yes"
            rvShops.adapter!!.notifyItemChanged(pos!!)
        }else
        {
            showSnackBar(this, response.message)
        }
    }
    private fun showAlertDialog() {
        val dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_aleart)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        val tvYes = dialog.findViewById<TextView>(R.id.tvYes)
        tvYes.setOnClickListener {
            dialog.dismiss()
            shopViewModel.addToCartApi(product_id!!, seller_id!!, "yes", prefs.jwtToken!!)
        }

        val tvNo = dialog.findViewById<TextView>(R.id.tvNo)
        tvNo.setOnClickListener {
            dialog.dismiss()
            rvShops.adapter!!.notifyItemChanged(pos!!)
        }

        dialog.show()
    }
    private fun setDataToUi(response: ResponseBean?) {
        // Disable options
        val optionManger = LinearLayoutManager(this)
        optionManger.orientation=LinearLayoutManager.HORIZONTAL
        rvOptions.layoutManager=optionManger
        rvOptions.adapter=HomeOptionAdapter(this, "", response!!.storeModel!!.type, null)
        rvOptions.scheduleLayoutAnimation()

        setRoundImage(this, ivShop, lvShopDetail, "" + response!!.storeModel!!.image)
        tvShopName.text=response!!.storeModel!!.name
        tvAddress.text=response!!.storeModel!!.address
        tvRating.text=""+response!!.storeModel!!.rating
        tvTime.text=""+response!!.storeModel!!.deliveryTime+" "+getString(R.string.days)
        if(response.review!!.size==0) tvReviews.text="(+ "+response.review!!.size +" "+getString(R.string.review)+ " )"
        else tvReviews.text="(+ "+response.review!!.size +" "+getString(R.string.review)+" )"
        setRoundImage(this, ivShopCover, lvShopCoverDetail, "" + response!!.storeModel!!.cover_image)

        // Select Option
        val selectOption = LinearLayoutManager(this)
        selectOption.orientation=LinearLayoutManager.HORIZONTAL
        rvOptionsSelection.layoutManager=selectOption
        rvOptionsSelection.adapter=HomeOptionAdapter(this, "Home", response!!.storeModel!!.type, this)
        rvOptionsSelection.scheduleLayoutAnimation()

        if(response.storeModel!!.favourite.equals("yes")) ivHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bitmap_fav_in))
        else ivHeart.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.bitmap_fav_out))
    }
    private fun getGuestAddedProduct(list: List<CommonProductItemModel>?): List<CommonProductItemModel>? {
        for(i in 0..(list!!.size-1))
        {
            if(GuestData.instance!!.allData!!.size>0){
                for(j in 0..(GuestData.instance!!.allData!!.size-1)){
                    if(GuestData!!.instance!!.allData!!.get(j).product_id.equals(list.get(i).id))
                    {
                        list.get(i).havecart="yes"
                        haveCart=true
                        count=count+1
                        totalPrice=totalPrice+list.get(i).sp!!
                    }
                }
            }
            else{ break }
        }

        return list
    }
    private fun setProductByCategory(response: ResponseBean?) {

        // Selected Option
        categoryList=response!!.list!!
        cartDetailModel=response?.cart_details
        for(i in 0..categoryList!!.size-1){
            if(categoryList!!.get(i).havecart.equals("yes")){
                haveCart=true
                count=count+1
                totalPrice=totalPrice+categoryList!!.get(i).sp!!
            }
        }
        if(prefs.jwtToken.equals(""))
        {
            categoryList=getGuestAddedProduct(categoryList)
        }

//        if(haveCart){
////            ivCart.visibility=View.VISIBLE
//            clViewCart.visibility=View.VISIBLE
//            tvItemsCount.text=""+count+" "+getString(R.string.item)
//            tvTotalPrice.text=getString(R.string.sar)+" "+totalPrice
//            if(count>1){
//                tvItemsCount.text=""+ count+" "+getString(R.string.items)
//            }
//        }else{
//            ivCart.visibility=View.GONE
//            clViewCart.visibility=View.GONE
//        }

        if(cartDetailModel?.total?:0.0>0){
            clViewCart.visibility=View.VISIBLE
            if(cartDetailModel?.total_itmes?:0.0>1) tvItemsCount.text=""+Math.round(cartDetailModel?.total_itmes ?: 0.0)+" "+getString(R.string.items)
            else tvItemsCount.text=""+Math.round(cartDetailModel?.total_itmes ?: 0.0)+" "+getString(
                R.string.item)
            tvTotalPrice.text=getString(R.string.sar)+" "+Math.round(cartDetailModel?.total ?: 0.0)
        }

        Collections.shuffle(categoryList)
        val shopsManager = LinearLayoutManager(this)
        rvShops.layoutManager = shopsManager
        rvShops.adapter = ProductAdapter(this, categoryList, this)
        rvShops.scheduleLayoutAnimation()

    }
    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnAddtoCart -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("cameFrom", ShopDetailsActivity::class.simpleName)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

            R.id.tvReview -> {
                val intent = Intent(this, ReviewActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("type", ParamEnum.STORE_TYPE.theValue() as String)
                intent.putExtra("id", getIntent().getStringExtra("store_id")!!)
                startActivity(intent)
            }

            R.id.ivFav -> {
                if (prefs.jwtToken.equals("")) {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    showLoadingDialog(this)
                    shopViewModel.addToWishApi(prefs.jwtToken!!, intent.getStringExtra("store_id")!!, "" + ParamEnum.STORE.theValue())
                }
            }
        }
    }
    override fun onSelectedOtion(option: String, pos: Int) {
       shopViewModel.productsTypeApi(this, intent.getStringExtra("store_id")!!, option.toLowerCase(), prefs.jwtToken!!, cart_id, quantity)
    }
    override fun onCart(pos: Int, product_id: String, seller_id: String) {
        this.pos=pos;
        this.product_id=product_id
        this.seller_id=seller_id
        shopViewModel.addToCartApi(product_id, seller_id, "no", prefs.jwtToken!!)
    }
    override fun onFav(pos: Int, product_id: String) {
        this.pos=pos
        if(prefs.jwtToken.equals("")) startActivity(this, LoginActivity::class.java)
        else shopViewModel.addToWishApi(prefs.jwtToken!!, product_id, "" + ParamEnum.PRODUCT.theValue())
    }
}
