package com.frzah.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.model.response.success.OrderModel
import kotlinx.android.synthetic.main.adapter_order_status.view.*

class OrderStatusAdapter(var context: Context,var list:List<OrderModel>) : RecyclerView.Adapter<OrderStatusAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderStatusAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_order_status, parent, false))
    override fun getItemCount(): Int= list.size
    override fun getItemViewType(position: Int): Int = position
    override fun getItemId(position: Int): Long = position.toLong()
    override fun onBindViewHolder(holder: OrderStatusAdapter.MyViewHolder, position: Int) {
        holder.itemView.rbStatus.text=list.get(position).name
        holder.itemView.rbStatus.isChecked=list.get(position).isChecked
        if(position==list.size-1) holder.itemView.view.visibility=View.GONE
    }

    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder){
    }

}