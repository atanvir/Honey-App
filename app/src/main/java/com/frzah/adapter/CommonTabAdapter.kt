
package com.frzah.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.activity.ProductDetail.ProductDetailActivity
import com.frzah.activity.ShopDetail.ShopDetailsActivity
import com.frzah.model.response.success.ProductDetailModel
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.ParamEnum
import com.frzah.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.adapter_common_product.view.*
import kotlinx.android.synthetic.main.adapter_common_stores.view.*

class CommonTabAdapter(var context: Context,var screen: String,var positions: Int,var list: MutableList<ProductDetailModel>,var listner:setOnUnFavClickListner) : RecyclerView.Adapter<CommonTabAdapter.MyViewHolder>() {

    var prefs:SharedPreferenceUtil= SharedPreferenceUtil.getInstance(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonTabAdapter.MyViewHolder {
        if(positions==0) return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_common_product, parent, false),positions)
        else return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_common_stores, parent, false),positions)
    }

    override fun getItemCount(): Int=list.size
    override fun getItemId(position: Int):Long =position.toLong()
    override fun getItemViewType(position: Int): Int = this.positions

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: CommonTabAdapter.MyViewHolder, position: Int) {
        if(!screen.equals("Search")) {
            // Fav Screen
            if(holder.itemViewType==1)
            {
                holder.itemView.ivHeartStore.visibility=View.GONE
                holder.itemView.ivContainer.visibility=View.GONE
                holder.itemView.ivDelete.visibility=View.VISIBLE
            }

            if(holder.itemViewType==0) holder.itemView.tvCount.text=context.getString(R.string.saved)+list.size+" "+context.getString(R.string.product)
            else holder.itemView.tvCountStore.text=context.getString(R.string.saved)+(list.size-1)+" "+context.getString(R.string.store)
        }
        else{
            if(holder.itemViewType==0) {
                if(list.get(position).favourite.equals("no")) {
                    holder.itemView.ivHeartProduct.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bitmap_fav_out))
                }else{
                    holder.itemView.ivHeartProduct.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bitmap_fav_in))

                }
                holder.itemView.tvCount.text=""+list.size+" "+context.getString(R.string.product)
                }
            else {
                if(list.get(position).favourite.equals("no")) {
                    holder.itemView.ivHeartStore.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bitmap_fav_out))
                }else{
                    holder.itemView.ivHeartStore.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.bitmap_fav_in))
                }
                holder.itemView.tvCountStore.text=""+(list.size-1)+" "+context.getString(R.string.store)
            }
        }

        if(holder.itemViewType==1) {
            setRoundImage(context,holder.itemView.ivStorePic,holder.itemView.lvIvStore, list.get(position).images!!)
            holder.itemView.tvRating.setText(""+list.get(position).rating)
            holder.itemView.tvStoreName.setText(""+list.get(position).name)
            holder.itemView.tvDeliveryTime.setText(""+list.get(position).delivery_time+" "+context.getString(R.string.days))
            // Store
            // Options RecycleView
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation= LinearLayoutManager.HORIZONTAL
            holder.itemView.rvOptions.layoutManager=layoutManager
            holder.itemView.rvOptions.adapter=HomeOptionAdapter(context,"", list.get(position).type,null)
            if(position==0) {
                holder.itemView.cardView.visibility=View.GONE
                holder.itemView.tvCountStore.visibility=View.VISIBLE
            }else
            {
                holder.itemView.cardView.visibility=View.VISIBLE
                holder.itemView.tvCountStore.visibility=View.GONE
            }
        }
        else {
            setRoundImage(context,holder.itemView.ivProduct,holder.itemView.lvProduct, list.get(position).images!!)
            holder.itemView.tvPercentage.setText(Math.round(list.get(position).discount!!.toDouble()).toString()+"%")

            if(Math.round(list.get(position).discount!!.toFloat())<=0.0)
            {
                holder.itemView.tvPercentage.visibility=View.INVISIBLE
                holder.itemView.tvMRP.visibility=View.INVISIBLE
            }
            if(prefs.selectedLanguage.equals("en",ignoreCase = true)) holder.itemView.tvSellingPrice.setText(context.getString(R.string.sar)+" "+list.get(position).sp!!)
            else holder.itemView.tvSellingPrice.setText(list.get(position).sp!!+" "+context.getString(R.string.sar))
            holder.itemView.tvMRP.paintFlags=holder.itemView.tvMRP.getPaintFlags() or Paint.STRIKE_THRU_TEXT_FLAG
            holder.itemView.tvMRP.setText(context.getString(R.string.sar)+" "+list.get(position).mrp!!)

            if(list.get(position)?.name!=null) {
                holder.itemView.tvProduct.setText(list.get(position).name!!)
            }
            if(list.get(position).categoryId!=null){
            holder.itemView.tvCategoryProduct.setText(list.get(position).categoryId!!)}

            if (position != 0)
            {
                holder.itemView.tvCount.visibility = View.GONE
            }
            else {
                holder.itemView.tvCount.visibility = View.VISIBLE
            }
        }
    }

    inner class  MyViewHolder(viewHolder: View,position:Int): RecyclerView.ViewHolder(viewHolder), View.OnClickListener {
        init {
            if(position==1){
                itemView.ivDelete.setOnClickListener(this)
                itemView.cardView.setOnClickListener(this)
                itemView.ivContainer.setOnClickListener(this)
            }else{
                itemView.ivFavProduct.setOnClickListener(this)
                itemView.cvContainer.setOnClickListener(this)
            }
        }

        override fun onClick(v: View?) {
            when(v!!.id)
            {
                R.id.cvContainer ->{
                    val intent=Intent(context,ProductDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("product_id",list.get(adapterPosition).id)
                    intent.putExtra("seller_id",list.get(adapterPosition).seller_id)
                    context.startActivity(intent)
                }
                R.id.cardView ->{
                    val intent=Intent(context,ShopDetailsActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra("store_id",list.get(adapterPosition).id)
                    context.startActivity(intent)
                }

                R.id.ivFavProduct -> { onFavClick(adapterPosition,ParamEnum.PRODUCT.theValue()) }
                R.id.ivDelete -> { onFavClick(adapterPosition,ParamEnum.STORE.theValue()) }
                R.id.ivContainer->{ onFavClick(adapterPosition,ParamEnum.STORE.theValue()) }
            }
        }
    }

    private fun onFavClick(adapterPosition: Int, type: Any?) {
        listner.onFav(adapterPosition, positions, list.get(adapterPosition).id!!, ""+ type)
        if(!screen.equals("Search")) { list.removeAt(adapterPosition) }
        else {
            if(list.get(adapterPosition).favourite.equals("no")){ list.get(adapterPosition).favourite="yes" }
            else{ list.get(adapterPosition).favourite="no" }
        }
    }

    interface setOnUnFavClickListner{
       fun onFav(pos:Int,position: Int,_id:String,type:String)
    }
}