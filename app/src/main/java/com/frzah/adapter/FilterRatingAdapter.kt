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
import kotlinx.android.synthetic.main.adapter_filter_rating.view.*

class FilterRatingAdapter(var context: Context, var list: ArrayList<String>,var rating: String,var listner: setOnItemClickListner) : RecyclerView.Adapter<FilterRatingAdapter.MyViewHolder>(){
   var selectedPos=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterRatingAdapter.MyViewHolder =MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_filter_rating,parent,false))
    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: FilterRatingAdapter.MyViewHolder, position: Int) {
        holder.itemView.tvOptions.text=list.get(position)
        if(rating.equals(list.get(position))) selectedPos=position
        if (selectedPos == position) {
            holder.itemView.tvOptions.backgroundTintList = context.getColorStateList(R.color.app_theme_organe)
            holder.itemView.tvOptions.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        } else {
            holder.itemView.tvOptions.backgroundTintList = context.getColorStateList(R.color.common_grey)
            holder.itemView.tvOptions.setTextColor(ContextCompat.getColor(context,R.color.light_black_home))
        }

    }
    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder), View.OnClickListener {
        init {
            itemView.tvOptions.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
           when(v!!.id)
           {
               R.id.tvOptions ->{
                   rating=list.get(adapterPosition)
                   selectedPos = adapterPosition
                   listner.ratingClick(list.get(selectedPos))
               }
           }
        }
    }

    interface setOnItemClickListner{
        fun ratingClick(rating:String)
    }

}