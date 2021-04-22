package com.frzah.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.model.response.success.AttributeModel
import com.frzah.utils.CommonUtils.Companion.setNormalImage
import com.frzah.utils.ViewExtension.readMore
import kotlinx.android.synthetic.main.adapter_specification.view.*

class SpecificationAdapter(var context: Context,var list: List<AttributeModel>?): RecyclerView.Adapter<SpecificationAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecificationAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_specification,parent,false))
    override fun getItemCount(): Int= list!!.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int =position

    override fun onBindViewHolder(holder: SpecificationAdapter.MyViewHolder, position: Int) {
        if(!list!!.get(position).image.equals("")) setNormalImage(context,holder.itemView.ciPic,holder.itemView.lvSpec,list!!.get(position).image)
        else holder.itemView.lvSpec.visibility=View.GONE
        holder.itemView.tvName.text=list!!.get(position).labal
        holder.itemView.tvDescss.text=list!!.get(position).text
        readMore(holder.itemView.tvDescss)
    }

    inner class MyViewHolder(view : View) :RecyclerView.ViewHolder(view)
    {

    }
}