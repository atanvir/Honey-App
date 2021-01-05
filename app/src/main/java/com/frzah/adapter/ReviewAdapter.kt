package com.frzah.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import kotlinx.android.synthetic.main.adapter_review.view.*

class ReviewAdapter(var context: Context,var list: List<com.frzah.model.request.ReviewModel.ResponseBean>) : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_review,parent,false))
    override fun getItemCount(): Int= list.size
    override fun onBindViewHolder(holder: ReviewAdapter.MyViewHolder, position: Int) {
        if(list.get(position).user!!.name.equals("")) holder.itemView.tvName.text=context.getString(R.string.anonymous)
        else holder.itemView.tvName.text=list.get(position).user!!.name
        holder.itemView.tvDesc.text= list.get(position).review
        holder.itemView.tvDate.text=list.get(position).created_at
        setRoundImage(context,holder.itemView.ivPic,holder.itemView.lottieAnimation,""+list.get(position).user!!.image)
    }
    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder){
    }

}