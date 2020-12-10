package com.honey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honey.R
import com.honey.model.response.success.AttributeModel
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.setNormalImage
import com.honey.utils.ViewExtension
import com.honey.utils.ViewExtension.readMore
import kotlinx.android.synthetic.main.activity_product_detail.*
import kotlinx.android.synthetic.main.adapter_specification.view.*

class SpecificationAdapter(var context: Context,var list: List<AttributeModel>?): RecyclerView.Adapter<SpecificationAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_specification,parent,false))
    override fun getItemCount(): Int= list!!.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int =position

    override fun onBindViewHolder(holder: SpecificationAdapter.MyViewHolder, position: Int) {
        setNormalImage(context,holder.itemView.ciPic,holder.itemView.lvSpec,list!!.get(position).image)
        holder.itemView.tvName.text=list!!.get(position).labal
        holder.itemView.tvDesc.text=list!!.get(position).text
        readMore(context,holder.itemView.tvDesc, 2)
    }

    inner class MyViewHolder(view : View) :RecyclerView.ViewHolder(view)
    {

    }
}