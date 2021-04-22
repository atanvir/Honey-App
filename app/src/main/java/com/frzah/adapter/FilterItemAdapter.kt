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
import com.frzah.utils.CommonUtils.Companion.getKey
import com.frzah.utils.CommonUtils.Companion.getValue
import kotlinx.android.synthetic.main.adapter_filter.view.*

class FilterItemAdapter(var context: Context, var list: ArrayList<String>,var type: String,var selectedText: String,var listner:setOnFilterItemClickListner) : RecyclerView.Adapter<FilterItemAdapter.MyViewHolder>() {
    var selectedPos = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterItemAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_filter, parent, false))
    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: FilterItemAdapter.MyViewHolder, position: Int) {
        holder.itemView.tv.text=list.get(position)
        if(checkValue(selectedText,type).equals(list.get(position))) selectedPos=position
        if (selectedPos == position) {
            holder.itemView.tv.backgroundTintList = context.getColorStateList(R.color.app_theme_organe)
            holder.itemView.tv.setTextColor(ContextCompat.getColor(context, android.R.color.white))
        } else {
            holder.itemView.tv.backgroundTintList = context.getColorStateList(R.color.common_grey)
            holder.itemView.tv.setTextColor(ContextCompat.getColor(context,R.color.light_black_home)
            )
        }

    }

    private fun checkValue(selectedText: String, type: String): String {
        var value=""
        if(type.equals("Sort By")) value=getValue(selectedText=selectedText,context = context)
        else value=selectedText
        return value
    }

    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder),
        View.OnClickListener {
        init{
            itemView.tv.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id)
            {
                R.id.tv ->{
                    selectedText=checkKey(type,list.get(adapterPosition))
                    selectedPos = adapterPosition
                    listner.onFilterItemClick(type,checkKey(type,list.get(adapterPosition)),adapterPosition+1)
                }
            }
        }

        private fun checkKey(type: String,key: String): String {
            var keyName=""
            if(type.equals("Sort By")) keyName=getKey(key=key,context=context)
            else keyName=key
            return keyName
        }
    }

    interface setOnFilterItemClickListner{
        fun onFilterItemClick(type: String,keyValue: String,days:Int)
    }

}