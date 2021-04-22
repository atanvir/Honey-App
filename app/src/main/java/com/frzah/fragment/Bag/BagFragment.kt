package com.frzah.fragment.Bag

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.activity.Coupon.CouponActivity
import com.frzah.activity.Login.LoginActivity
import com.frzah.activity.Payment.PaymentActivity
import com.frzah.adapter.BagItemAdapter
import com.frzah.base.BaseFragment
import com.frzah.model.request.CommonModel
import com.frzah.model.response.success.CartListModel
import com.frzah.utils.CommonUtils.Companion.getGuestData
import com.frzah.utils.CommonUtils.Companion.showLoadingDialog
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.startActivity
import com.frzah.utils.ErrorUtil
import com.frzah.utils.GuestData
import com.frzah.utils.ParamEnum
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.fragment_bag.*
import kotlinx.android.synthetic.main.layout_billing_details.*

class BagFragment : BaseFragment(), View.OnClickListener, BagItemAdapter.setOnBagClickListner {
    private lateinit var bagViewModel: BagViewModel
    private var cart_id:String=""
    private var quantity:String=""
    private var seller_id:String=""
    private var cartData:CartListModel?=null
    val APPLY_COUPON_REQ=34
    private var isCouponApplied=false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bag,container,false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initControl()
        myObserver()
    }

    override fun init() {
        guestData()
        bagViewModel=ViewModelProvider(requireActivity()).get(BagViewModel::class.java)
        showLoadingDialog(requireActivity())
        bagViewModel.cartListApi(prefs.jwtToken!!,cart_id,quantity)
    }

    private fun guestData() {
        if(prefs.jwtToken!!.equals(""))
        {
            cart_id=getGuestData(""+ParamEnum.PRODUCT_ID.theValue())
            seller_id=getGuestData(""+ParamEnum.SELLER_ID.theValue())
            quantity=getGuestData(""+ParamEnum.QUANTITY.theValue())
        }
    }

    override fun initControl() {
        btnCheckout.setOnClickListener(this)
        clCoupon.setOnClickListener(this)
        ivArrowCoupon.setOnClickListener(this)
        PushDownAnim.setPushDownAnimTo(ivArrowCoupon).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        PushDownAnim.setPushDownAnimTo(clCoupon).setScale(PushDownAnim.MODE_SCALE, 0.89f)
        PushDownAnim.setPushDownAnimTo(btnCheckout).setScale(PushDownAnim.MODE_SCALE, 0.89f)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun myObserver() {

        bagViewModel.response.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(activity,it.message)
        })

        bagViewModel.updateCartResponse.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) responseHandler()
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(activity,it.message)
        })

        bagViewModel.removeCartResponse.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) responseHandler()
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(activity,it.message)
        })

        bagViewModel.removeCouponResponse.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) bagViewModel.cartListApi(prefs.jwtToken!!,cart_id,quantity)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(activity,it.message)
        })

        bagViewModel.error.observe(requireActivity(),Observer{ ErrorUtil.handlerGeneralError(requireActivity(), it) })
    }

    private fun responseHandler() {
        bagViewModel.cartListApi(prefs.jwtToken!!,cart_id,quantity)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setDataToUi(response: CommonModel?) {
        if(response!!.message.equals(getString(R.string.no_item_cart)))
        {
            mainCl.visibility = View.GONE
            clNoItem.visibility=View.VISIBLE
        }

        if (response.cart_list!!.cart_list != null) {
            if (response.cart_list.cart_list!!.size > 0) {
                mainCl.visibility = View.VISIBLE
                clNoItem.visibility = View.GONE
            } else {
                mainCl.visibility = View.GONE
                clNoItem.visibility=View.VISIBLE
            }

            if(response.cart_list.cart_list!!.size>0)
            {
                cartData=response!!.cart_list!!.cart_list!!.get(0)
            }

            val adapter = BagItemAdapter(requireContext(), response.cart_list.cart_list!!, this)
            rvCart.adapter = adapter
            adapter.notifyDataSetChanged()
            rvCart.scheduleLayoutAnimation()
            tvSubTotal.text = "" + response.cart_list!!.Cart_amount!!.sub_toal
            if(response.cart_list!!.Cart_amount!!.tax==0L) clTextFees.visibility=View.GONE
            else clTextFees.visibility=View.VISIBLE
            if(response.cart_list!!.Cart_amount!!.discounted_amount==0L)  {
                isCouponApplied=false
                clCoupon.backgroundTintList=requireActivity().getColorStateList(R.color.app_theme_organe)
                tvLabelCoupon.text=getString(R.string.apply_coupon)
                prefs.coupon_code=""
                ivArrowCoupon.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.bitmap_arrow))
                clDiscount.visibility=View.GONE
            }
            else { clDiscount.visibility=View.VISIBLE
                isCouponApplied=true
                clCoupon.backgroundTintList=requireActivity().getColorStateList(R.color.green)
                prefs.coupon_code=response.cart_list.cart_list!!.get(0).couponCode
                tvLabelCoupon.text=getString(R.string.applied_code)+" "+response.cart_list.cart_list!!.get(0).couponCode
                ivArrowCoupon.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.remove))
            }
            clShippingCharges.visibility=View.GONE
            tvTax.text = "" + response.cart_list!!.Cart_amount!!.tax
            tvTotal.text = "" + response.cart_list!!.Cart_amount!!.total
            tvDiscount.text = "" + response.cart_list!!.Cart_amount!!.discounted_amount
            if (response.cart_list.cart_list!!.size > 1) {
                tvItems.text = "( " + response.cart_list!!.cart_list!!.size + " "+getString(R.string.items)+" )"
            } else {
                tvItems.text = "( " + response.cart_list!!.cart_list!!.size + " "+getString(R.string.item)+")"
            }
        }
        else{
            clNoItem.visibility=View.VISIBLE
            mainCl.visibility = View.GONE
        }
    }
    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnCheckout ->{
                prefs.subTotal=tvSubTotal.text.toString()
                prefs.tax=tvTax.text.toString()
                prefs.total=tvTotal.text.toString()
                prefs.item=tvItems.text.toString()
                prefs.discount=tvDiscount.text.toString()
                Log.e("Latitute",""+cartData!!.latitude)
                Log.e("Longitude",""+cartData!!.longitude)
                prefs.latitude=""+cartData!!.latitude
                prefs.longitude= ""+cartData!!.longitude
                prefs.soldBy = ""+cartData!!.soldBy
                if(prefs.isLogin!!) startActivity(requireActivity(), PaymentActivity::class.java)
                else startActivity(requireActivity(), LoginActivity::class.java) }
            R.id.clCoupon ->{
                val intent=Intent(requireActivity(),CouponActivity::class.java)
                startActivityForResult(intent,APPLY_COUPON_REQ)
            }

            R.id.ivArrowCoupon ->{
                if(isCouponApplied) bagViewModel.removeCouponApi(requireActivity(),prefs.jwtToken!!)
            }
        }
    }
    override fun onRemove(position: Int, productId: String,quantity: Int) {
        if(prefs.jwtToken!!.equals(""))
        {
            GuestData.instance!!.removeData(productId)
            guestData()
            showLoadingDialog(requireActivity())
            bagViewModel.cartListApi(prefs.jwtToken!!,cart_id,this.quantity)
        }else {
            showLoadingDialog(requireActivity())
            bagViewModel.removeToCartApi(requireActivity(), productId, prefs.jwtToken!!, quantity)
        }
    }
    override fun updateCart(position: Int, productId: String, quantity: Int) {
        if(prefs.jwtToken!!.equals(""))
        {
            GuestData.instance!!.updateQuantity(quantity.toLong(),productId)
            guestData()
            showLoadingDialog(requireActivity())
            bagViewModel.cartListApi(prefs.jwtToken!!,cart_id,this.quantity)
        }
        else {
            showLoadingDialog(requireActivity())
            bagViewModel.updateCartApi(requireActivity(), productId, prefs.jwtToken!!, quantity)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            APPLY_COUPON_REQ ->{
             if(resultCode==RESULT_OK)
             {
                 showLoadingDialog(requireActivity())
                 bagViewModel.cartListApi(prefs.jwtToken!!,cart_id,quantity)
             }

            }
        }
    }

}