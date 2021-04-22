package com.frzah.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.model.response.success.ProductDetailModel
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import kotlinx.android.synthetic.main.adapter_bag_item.view.ivProductPic
import kotlinx.android.synthetic.main.adapter_bag_item.view.lvCart
import kotlinx.android.synthetic.main.adapter_bag_item.view.tvProductDescription
import kotlinx.android.synthetic.main.adapter_bag_item.view.tvProductName
import kotlinx.android.synthetic.main.adapter_bag_item.view.tvSellingPrice
import kotlinx.android.synthetic.main.adapter_product_offer.view.*

class OfferProductAdapter(var context: Context, var list: List<ProductDetailModel>): RecyclerView.Adapter<OfferProductAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferProductAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_product_offer,parent,false))
    override fun getItemCount(): Int=list.size
    override fun getItemId(position: Int): Long =position.toLong()
    override fun getItemViewType(position: Int):Int=position
    override fun onBindViewHolder(holder: OfferProductAdapter.MyViewHolder, position: Int) {
        setRoundImage(context,holder.itemView.ivProductPic,holder.itemView.lvCart,list.get(position).images!!)
        holder.itemView.tvProductName.text=list.get(position).name
        holder.itemView.tvProductDescription.text=list.get(position).categoryId
        holder.itemView.tvSellingPrice.text=context.getString(R.string.sar)+" "+list.get(position).mrp
        holder.itemView.tvQuantity.text="x"+list.get(position).quantity
    }
   inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder) {
    }

}