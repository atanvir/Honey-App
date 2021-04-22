package com.frzah.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.activity.Login.LoginActivity
import com.frzah.activity.OfferDetail.OfferDetailActivity
import com.frzah.activity.ShopDetail.ShopDetailsActivity
import com.frzah.model.response.success.CommonShopsItemModel
import com.frzah.utils.CommonUtils.Companion.OFFERS
import com.frzah.utils.CommonUtils.Companion.SHOPS
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.SharedPreferenceUtil
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.adapter_home_offer.view.*
import kotlinx.android.synthetic.main.adapter_home_shop.view.*

class CommonHomeAdapter(var context: Context, var list: List<CommonShopsItemModel>, var option:String, var listner: setOnShopClickListner) : RecyclerView.Adapter<CommonHomeAdapter.MyViewHolder>()
{
    val prefs=SharedPreferenceUtil.getInstance(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
    if(option.equals(context.getString(R.string.offers),ignoreCase = true)) MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_home_offer,parent,false))
    else  MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_home_shop,parent,false))
    override fun getItemCount(): Int=list.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int =if(option.equals(context.getString(R.string.offers),ignoreCase = true)) OFFERS else SHOPS
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when(holder.itemViewType){
            SHOPS-> {
                holder.itemView.lvFavHomeShop.visibility=View.GONE
                if(list.get(position).favourite.equals("yes")) holder.itemView.ivHeartHome.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bitmap_fav_in))
                else holder.itemView.ivHeartHome.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bitmap_fav_out))
                holder.itemView.tvRating.text=""+list.get(position).rating
                holder.itemView.tvReview.text="(+"+list.get(position).ratingCount+")"
                holder.itemView.tvShopName.text=""+list.get(position).name
                holder.itemView.tvDeliverTime.text=""+list.get(position).deliveryTime+" "+context.getString(R.string.days)
                setRoundImage(context,holder.itemView.ivShops,holder.itemView.lottieAnimation,list.get(position).image!!)
            }

            OFFERS-> {
                setRoundImage(context,holder.itemView.ivOffers,holder.itemView.lvOffers,list.get(position).image!!)
                holder.itemView.tvOfferTitle.text=list.get(position).name
                holder.itemView.tvOfferDes.text=list.get(position).description
                holder.itemView.tvSPOffer.text=context.getString(R.string.sar)+" "+list.get(position).mrp
                holder.itemView.tvMRPOffer.paintFlags=holder.itemView.tvMRPOffer.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
                holder.itemView.tvMRPOffer.text=context.getString(R.string.sar)+" "+list.get(position).offer_price
            }
        }
    }
    inner class  MyViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
         init {
             when(option.toLowerCase()){
                 "offers" ->{
                     //Offers
                     view.clMainOffers.setOnClickListener(this)
                 }
                 else ->{
                    //Shops
                     view.clMain.setOnClickListener(this)
                     view.ivFav.setOnClickListener(this)
                     PushDownAnim.setPushDownAnimTo(view.clMain).setScale(PushDownAnim.MODE_SCALE, 0.89f)
                 }
             }
         }

         override fun onClick(v: View?) {
             when(v!!.id)
             {
                 //Offers
                 R.id.clMainOffers-> {
                     val intent:Intent?
                     if(prefs.jwtToken!!.equals("")) intent = Intent(context, LoginActivity::class.java)
                     else intent = Intent(context, OfferDetailActivity::class.java)
                     intent.putExtra("offer_id", "" + list.get(adapterPosition).id)
                     intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                     context.startActivity(intent)
                 }

                 //Shops
                 R.id.clMain -> { storeDetailIntent(""+list.get(adapterPosition).id) }

                 R.id.ivFav -> {
                   if(prefs.jwtToken!!.equals("")) {
                         val intent = Intent(context, LoginActivity::class.java)
                         intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                         context.startActivity(intent)
                     }
                   else {
                         if(!list.get(adapterPosition).favourite.equals("yes")) {
                             itemView.lvFavHomeShop.visibility = View.VISIBLE
                         }
                         listner.onFav(adapterPosition,""+list.get(adapterPosition).id)
                     }
                 }
             }
         }
     }
    private fun storeDetailIntent(store_id:String) {
        val intent=Intent(context,ShopDetailsActivity::class.java)
        intent.putExtra("store_id",store_id)
        intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
    interface setOnShopClickListner{
        fun onFav(pos: Int,storeId: String)
    }
}