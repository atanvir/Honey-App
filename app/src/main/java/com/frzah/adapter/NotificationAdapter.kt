package com.frzah.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.activity.OrderDetail.OrderDetailActivity
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.CommonUtils.Companion.getTimeAgo
import com.frzah.utils.ParamEnum
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
        holder.itemView.tvTime.text=getTimeAgo(""+list.get(position).createdAt,context = context)
    }

    inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder),
        View.OnClickListener {
        init{
            itemView.mainCl.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id){
                R.id.mainCl ->{
                val intent= Intent(context,OrderDetailActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra(ParamEnum.ORDER_ID.theValue() as String,""+list.get(adapterPosition).order_id)
                intent.putExtra("cameFrom",getWhereFrom(list.get(adapterPosition).content))
                context.startActivity(intent)
              }
            }
        }
    }

    private fun getWhereFrom(content: String?): String? {
        when(content){
            "Your order has been Delivered" ->{ return ""}
            "Your order has been cancelled" ->{ return ""}
            else ->{ return ""}
        }
    }

}