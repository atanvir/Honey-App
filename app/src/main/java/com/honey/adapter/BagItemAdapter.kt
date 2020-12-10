package com.honey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honey.R
import com.honey.activity.Main.MainActivity
import com.honey.activity.OrderDetail.OrderDetailActivity
import com.honey.model.response.success.CartListModel
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.setRoundImage
import kotlinx.android.synthetic.main.adapter_bag_item.view.*

class BagItemAdapter(var context: Context,var list: List<CartListModel>,var listner: setOnBagClickListner?): RecyclerView.Adapter<BagItemAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BagItemAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_bag_item,parent,false))
    override fun getItemCount(): Int=list.size
    override fun getItemId(position: Int): Long =position.toLong()
    override fun getItemViewType(position: Int):Int=position
    override fun onBindViewHolder(holder: BagItemAdapter.MyViewHolder, position: Int) {
        setRoundImage(context, holder.itemView.ivProductPic, holder.itemView.lvCart, list.get(position).image!!)
        holder.itemView.tvProductName.text=list.get(position).name!!
        holder.itemView.tvProductDescription.text=list.get(position).category_id!!
        holder.itemView.tvSellingPrice.text =context.getString(R.string.sar)+" "+list.get(position).sp!!
        if(context is OrderDetailActivity)
        {
            // Order Details Items
            holder.itemView.ivRemove.visibility=View.GONE
            holder.itemView.ivMinus.visibility=View.GONE
            holder.itemView.viewMinus.visibility=View.GONE
            holder.itemView.ivPlus.visibility=View.GONE
            holder.itemView.viewPlus.visibility=View.GONE
            holder.itemView.viewPlus2.visibility=View.GONE
            holder.itemView.tvQuatity.visibility=View.GONE
            holder.itemView.tvQuatityDetailPage.visibility=View.VISIBLE
            holder.itemView.tvQuatityDetailPage.text="x"+list.get(position).quantity!!

        }else{
            // Cart Item
            holder.itemView.tvQuatity.text=""+list.get(position).quantity!!
        }
    }
   inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder), View.OnClickListener {
        init {
            viewHolder.ivRemove.setOnClickListener(this)
            viewHolder.ivPlus.setOnClickListener(this)
            viewHolder.ivMinus.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id)
            {
                R.id.ivRemove ->{ listner!!.onRemove(adapterPosition, ""+list.get(adapterPosition).productId!!,list.get(adapterPosition).quantity!!.toInt()) }
                R.id.ivPlus ->{ listner!!.updateCart(adapterPosition,""+list.get(adapterPosition).productId!!,(list.get(adapterPosition).quantity!!.toInt()+1)) }
                R.id.ivMinus ->{
                    if(list.get(adapterPosition).quantity!!.toInt()==1) listner!!.onRemove(adapterPosition,""+list.get(adapterPosition).productId!!,list.get(adapterPosition).quantity!!.toInt())
                    else listner!!.updateCart(adapterPosition,""+list.get(adapterPosition).productId!!,(list.get(adapterPosition).quantity!!.toInt()-1))
                }
            }
        }
    }

    interface setOnBagClickListner{
        fun onRemove(position: Int,productId: String,quantity: Int)
        fun updateCart(position: Int,productId: String,quantity:Int)
    }

}