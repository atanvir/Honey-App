package com.honey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honey.R
import com.honey.model.response.success.ResponseBean
import com.honey.model.response.success.ReviewModel
import com.honey.utils.CommonUtils
import kotlinx.android.synthetic.main.adapter_review.view.*

class ReviewAdapter(var context: Context,var list: List<com.honey.model.request.ReviewModel.ResponseBean>) : RecyclerView.Adapter<ReviewAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_review,parent,false))
    override fun getItemCount(): Int= list.size
    override fun onBindViewHolder(holder: ReviewAdapter.MyViewHolder, position: Int) {
        holder.itemView.tvDesc.text= list.get(position).review
        holder.itemView.tvDate.text=list.get(position).created_at
        holder.itemView.tvName.text=list.get(position).user!!.name
        CommonUtils.setRoundImage(context,holder.itemView.ivPic,holder.itemView.lottieAnimation,""+list.get(position).user!!.image)
    }
    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder){
    }

}