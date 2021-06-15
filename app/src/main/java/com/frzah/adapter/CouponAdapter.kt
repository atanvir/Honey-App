package com.frzah.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.CommonUtils.Companion.getRandomColor
import com.frzah.utils.ParamEnum
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.adapter_coupon.view.*

class CouponAdapter(var context: Context, var list: List<ResponseBean>,var listner: setOnCouponCodeClickListner) : RecyclerView.Adapter<CouponAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_coupon, parent, false))
    override fun getItemCount(): Int=list.size
    override fun getItemId(position: Int): Long =position.toLong()
    override fun getItemViewType(position: Int):Int=position

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CouponAdapter.MyViewHolder, position: Int) {
        holder.itemView.ivOffer.backgroundTintList=ColorStateList.valueOf(getRandomColor())
        holder.itemView.tvCode.text=context.getString(R.string.take)+" " + getDiscountType(list.get(position).discount?:"", list.get(position).discount_type) +" "+ context.getString(R.string.off) + context.getString(R.string.coupon_code) + list.get(position).coupon_code?:""
        holder.itemView.tvExpireOn.text=context.getString(R.string.valid_till)+list.get(position).expiry_date
        holder.itemView.tvMinPrice.text=context.getString(R.string.min_price)+" "+context.getString(R.string.sar)+" "+list.get(position).min_price?:""
    }

    private fun getDiscountType(discount: String?, type: String?): String? {
        return if (type.equals(ParamEnum.PRICE.theValue() as String, ignoreCase = true)) context.getString(R.string.sar)+" $discount" else "$discount%"
    }


    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            PushDownAnim.setPushDownAnimTo(itemView.mainCl).setScale(PushDownAnim.MODE_SCALE, 0.89f)
            itemView.mainCl.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            listner.setCode(list.get(adapterPosition).coupon_code!!)
        }

    }

    interface setOnCouponCodeClickListner{
        fun setCode(couponCode:String)
    }




}