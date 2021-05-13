package com.frzah.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.adapter_home_option.view.*

class HomeOptionAdapter(var context: Context, val screen: String, val data: List<String>?,var listner: onOptionClickListner?) : RecyclerView.Adapter<HomeOptionAdapter.MyViewHolder>(){
    var selectedPos=0;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder= MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_home_option, parent, false))
    override fun getItemId(position: Int): Long =position.toLong();
    override fun getItemViewType(position: Int): Int =position
    override fun getItemCount(): Int=data?.size?:0;

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if(screen.equals("Home")) {
            if (selectedPos == position) {
                holder.itemView.tvOptions.backgroundTintList = context.getColorStateList(R.color.app_theme_organe)
                holder.itemView.tvOptions.setTextColor(ContextCompat.getColor(context, android.R.color.white))
            } else {
                holder.itemView.tvOptions.backgroundTintList = context.getColorStateList(R.color.common_grey)
                holder.itemView.tvOptions.setTextColor(ContextCompat.getColor(context, R.color.cool_grey))
            }
        }else
        {
            holder.itemView.tvOptions.backgroundTintList = context.getColorStateList(R.color.common_grey)
            holder.itemView.tvOptions.setTextColor(ContextCompat.getColor(context, R.color.light_black_home))
        }
        holder.itemView.tvOptions.text=data!!.get(position).trim()
    }

     inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder), View.OnClickListener {
         init {
             viewHolder.tvOptions.setOnClickListener(this)
             PushDownAnim.setPushDownAnimTo(viewHolder.clMain2).setScale(PushDownAnim.MODE_SCALE, 0.89f)
         }

         override fun onClick(v: View?) {
             when(v!!.id)
             {
                 R.id.tvOptions -> {
                     if (selectedPos != adapterPosition) {
                         selectedPos = adapterPosition
                         if (listner != null) listner!!.onSelectedOtion(data!!.get(adapterPosition),adapterPosition)
                         notifyDataSetChanged()
                     }
                 }
             }
         }
     }

    interface onOptionClickListner{
        fun  onSelectedOtion(option: String,position: Int)
    }

}
