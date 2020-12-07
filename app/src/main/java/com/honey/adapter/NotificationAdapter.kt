package com.honey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honey.R
import com.honey.model.response.success.ResponseBean
import com.honey.utils.CommonUtils
import kotlinx.android.synthetic.main.adapter_notification.view.*

class NotificationAdapter(var context: Context,var list:List<ResponseBean>) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_notification,parent,false))
    override fun getItemCount(): Int= list.size
    override fun getItemViewType(position: Int): Int =position
    override fun getItemId(position: Int): Long =position.toLong()

    override fun onBindViewHolder(holder: NotificationAdapter.MyViewHolder, position: Int) {
        if(position!=0) holder.itemView.ivDot.visibility=View.GONE
        holder.itemView.tvTitle.text=list.get(position).title
        holder.itemView.tvContent.text=list.get(position).content
        holder.itemView.tvTime.text=CommonUtils.getTimeAgo(""+list.get(position).createdAt)
    }

    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder){
    }

}