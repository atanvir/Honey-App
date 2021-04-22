package com.frzah.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.activity.Login.LoginActivity
import com.frzah.activity.ProductDetail.ProductDetailActivity
import com.frzah.model.request.GuestDataModel
import com.frzah.model.response.success.CommonProductItemModel
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.GuestData
import com.frzah.utils.SharedPreferenceUtil
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.adapter_product.view.clMain
import kotlinx.android.synthetic.main.adapter_product_vertical.view.*

class ProductAdapter(var context: Context, var list: List<CommonProductItemModel>?, var listner: onProductClickListner) : RecyclerView.Adapter<ProductAdapter.MyViewHolder>(){
    private val prefs=SharedPreferenceUtil.getInstance(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.MyViewHolder =  MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_product_vertical, parent, false))
    override fun getItemCount(): Int= list?.size?:0
    override fun getItemId(position: Int)=position.toLong()
    override fun getItemViewType(position: Int)=position

    override fun onBindViewHolder(holder: ProductAdapter.MyViewHolder, position: Int) {
        holder.itemView.tvMrpVertical.paintFlags = holder.itemView.tvMrpVertical.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
        setRoundImage(context, holder.itemView.ivShopVerical, holder.itemView.lvProductPicVertical, "" + list!!.get(position).images)
        holder.itemView.tvDiscountVertical.text=""+Math.round(list!!.get(position).discount!!.toFloat())+"%"
        if(Math.round(list!!.get(position).discount!!.toFloat())<=0.0)
        {
            holder.itemView.tvDiscountVertical.visibility=View.INVISIBLE
            holder.itemView.tvMrpVertical.visibility=View.INVISIBLE
        }
        holder.itemView.tvRating.text=" "+list!!.get(position).rating
        if(list!!.get(position).review!!.size>0)  holder.itemView.tvReviewss.text="( +"+list!!.get(position).review!!.size+" "+context.getString(R.string.reviews)+" )"
        else holder.itemView.tvReviewss.text="( +"+list!!.get(position).review!!.size+" "+context.getString(R.string.review)+" )"
        holder.itemView.tSpVertical.text =context.getString(R.string.sar)+" "+list!!.get(position).sp
        holder.itemView.tvMrpVertical.text =context.getString(R.string.sar)+" "+list!!.get(position).mrp
        holder.itemView.tvProductNameVertical.text=""+list!!.get(position).name
        holder.itemView.tvCategoryVertical.text=""+list!!.get(position).categoryId
        holder.itemView.lvFavVertical.visibility=View.GONE
        holder.itemView.btnAddCardVertical.isEnabled=true
        holder.itemView.ivFavVertical.isEnabled=true
        if(list!!.get(position).favourite.equals("yes")) holder.itemView.ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bitmap_fav_in))
        else holder.itemView.ivHeart.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.bitmap_fav_out))
    }

    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder), View.OnClickListener {
        init {
            PushDownAnim.setPushDownAnimTo(viewHolder.clMain).setScale(PushDownAnim.MODE_SCALE, 0.89f)
            viewHolder.clMain.setOnClickListener(this)
            viewHolder.btnAddCardVertical!!.setOnClickListener(this)
            viewHolder.ivFavVertical!!.setOnClickListener(this)
            }

        override fun onClick(v: View?) {
            when (v!!.id) {

                R.id.clMain -> {
                    val intent = Intent(context, ProductDetailActivity::class.java)
                    intent.putExtra("product_id", "" + list!!.get(adapterPosition).id)
                    intent.putExtra("seller_id", "" + list!!.get(adapterPosition).sellerId)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    context.startActivity(intent!!)
                }

                R.id.ivFavVertical -> {
                    if (prefs.jwtToken.equals("")) {
                        val intent = Intent(context, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    } else {
                        Handler().post(Runnable {
                            if (!list!!.get(adapterPosition).favourite.equals("yes")) {
                                itemView.lvFavVertical.visibility = View.VISIBLE
                            }
//                            itemView.lvFavVertical.getLayoutParams().width = 450
//                            itemView.lvFavVertical.getLayoutParams().height = 450
//                            itemView.lvFavVertical.requestLayout()
                            itemView.ivFavVertical.isEnabled = false
                            itemView.lvFavVertical.setAnimation("fav.json")
                            listner.onFav(adapterPosition, list!!.get(adapterPosition).id!!)
                        })
                    }
                }

                R.id.btnAddCardVertical -> {
//                    itemView.lvFavVertical.getLayoutParams().width = 120
//                    itemView.lvFavVertical.getLayoutParams().height = 120
//                    itemView.lvFavVertical.requestLayout()
                    itemView.btnAddCardVertical.isEnabled = false
                    itemView.lvFavVertical.setAnimation("loader.json")
                    itemView.lvFavVertical.visibility = View.VISIBLE
                    if (prefs.jwtToken!!.equals("")) saveGuestData(adapterPosition)
                    else listner.onCart(adapterPosition, list!!.get(adapterPosition).id!!, list!!.get(adapterPosition).sellerId!!)
                }
            }
        }
    }

    private fun saveGuestData(position: Int) {
        if(checkStore(position))
        {
        GuestData.instance!!.addData(GuestDataModel(list!!.get(position).id!!, list!!.get(position).sellerId!!, 1))
        list!!.get(position).havecart = "yes"
        notifyItemChanged(position)
        }else{
            showAlertDialog(position)
        }
    }
    private fun checkStore(position: Int): Boolean {
        var ret=true
        if(GuestData.instance!!.allData!!.size>0){
            for(i in 0..(GuestData.instance!!.allData!!.size-1)){
                if(!(list!!.get(position).sellerId.equals(GuestData.instance!!.allData!!.get(i).store_id))){
                    ret=false
                    break
                }
            }
        }
        return ret
    }
    private fun showAlertDialog(position: Int) {
        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_aleart)
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val tvYes = dialog.findViewById<TextView>(R.id.tvYes)
        tvYes.setOnClickListener {
            dialog.dismiss()
            GuestData.instance!!.removeAllData()
            GuestData.instance!!.addData(GuestDataModel(list!!.get(position).id!!, list!!.get(position).sellerId!!, 1))
            list!!.get(position).havecart="yes"
            notifyItemChanged(position)
        }

        val tvNo = dialog.findViewById<TextView>(R.id.tvNo)
        tvNo.setOnClickListener {
            dialog.dismiss()
            list!!.get(position).havecart="no"
            notifyItemChanged(position)
        }
        dialog.show()
    }
    interface onProductClickListner {
        fun onCart(pos: Int, product_id: String, seller_id: String)
        fun onFav(pos: Int, product_id: String)
    }
}