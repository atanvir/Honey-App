package com.frzah.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.activity.OrderDetail.OrderDetailActivity
import com.frzah.activity.Rating.RatingActivity
import com.frzah.fragment.Order.OrderViewModel
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.CommonUtils.Companion.getDispatchTime
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.ParamEnum
import com.frzah.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.adapter_history.view.*
import kotlinx.android.synthetic.main.adapter_upcoming.view.*

class CommonOrderAdapter(var context: Context,var pos:Int,var list:List<ResponseBean>,var orderViewModel:OrderViewModel) : RecyclerView.Adapter<CommonOrderAdapter.MyViewHolder>(){
    val prefs=SharedPreferenceUtil.getInstance(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonOrderAdapter.MyViewHolder {
        if(pos==0) return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_upcoming,parent,false))
        else return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_history,parent,false))
    }
    override fun getItemCount(): Int=list.size
    override fun getItemId(position: Int): Long=position.toLong()
    override fun getItemViewType(position: Int): Int=pos

    override fun onBindViewHolder(holder: CommonOrderAdapter.MyViewHolder, position: Int) {
        list.get(position).item_count= list.get(position).item_count!! -1
       when(holder.itemViewType)
       {
           0 ->{
               // Upcoming Orders
               setRoundImage(context,holder.itemView.ivSellerPhoto,holder.itemView.la, list.get(position).images!!)
               if(list.get(position).item_count==1L) holder.itemView.tvItemsCount.text=""+list.get(position).item_count+" "+context.getString(R.string.item)
               else holder.itemView.tvItemsCount.text=""+list.get(position).item_count+" "+context.getString(R.string.items)
               holder.itemView.tvSellerNames.text=list.get(position).seller_name
               if(prefs?.selectedLanguage.equals("en",ignoreCase = true))  holder.itemView.tvAmount.text=context.getString(R.string.sar)+" "+list.get(position).amount
               else holder.itemView.tvAmount.text=list.get(position).amount+" "+context.getString(R.string.sar)
               holder.itemView.tvStatus.text=list.get(position).status
               holder.itemView.tvOrderDate.text=context.getString(R.string.status)
               if(getDispatchTime(list.get(position).dispatch_at!!,context=context)!!.split(" ".toRegex()).size>1){
                   holder.itemView.tvDispatchTime.text = getDispatchTime(list.get(position).dispatch_at!!,context=context)!!.split(" ".toRegex())[0]
                   holder.itemView.tvNoOfDays.text = getDispatchTime(list.get(position).dispatch_at!!,context=context)!!.split(" ".toRegex())[1]
               }
           }
           1 -> {
               // Past Orders
               holder.itemView.tvDate.text = list.get(position).order_date
               if (list.get(position).item_count==1L) holder.itemView.tvItems.text = ""+list.get(position).item_count + " "+context.getString(R.string.item)
               else holder.itemView.tvItems.text = ""+list.get(position).item_count + " "+context.getString(R.string.items)
               setRoundImage(context, holder.itemView.ivSeller, holder.itemView.lv, list.get(position).images!!)
               holder.itemView.tvSellerName.text = list.get(position).seller_name
               if(prefs?.selectedLanguage.equals("en",ignoreCase = true))  holder.itemView.tvSellingPrice.text=context.getString(R.string.sar)+" "+list?.get(position)?.amount?:""
               else holder.itemView.tvSellingPrice.text = list?.get(position)?.amount?:""+" "+context.getString(R.string.sar)
               holder.itemView.tvOrderStaus.text = list?.get(position).status

               if (list.get(position).status.equals("Delivered", ignoreCase = true)){
                   holder.itemView.btnRate.visibility=View.VISIBLE
                   holder.itemView.tvOrderStaus.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.drawable_green_dot),null,null,null)
                   holder.itemView.tvOrderStaus.setTextColor(ContextCompat.getColor(context,R.color.green))
               }else {
                   holder.itemView.btnRate.visibility=View.GONE
                   holder.itemView.tvOrderStaus.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.drawable_red_dot),null,null,null)
                   holder.itemView.tvOrderStaus.setTextColor(ContextCompat.getColor(context,R.color.red))
               }
           }
       }

    }

    inner class MyViewHolder (view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            when(pos)
            {
               0 ->{
                   // Upcoming Order
                   itemView.cvUpcoming.setOnClickListener(this)
                   itemView.btnCancel.setOnClickListener(this)
               }

               1 ->{
                  // Past Order
                   itemView.cardView.setOnClickListener(this)
                   itemView.btnReOrder.setOnClickListener(this)
                   itemView.btnRate.setOnClickListener(this)
               }
            }
        }

        override fun onClick(v: View?) {
            when(v!!.id)
            {
                // Past Order
                R.id.cardView ->{
                    val intent = Intent(context, OrderDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(ParamEnum.ORDER_ID.theValue() as String,""+list.get(adapterPosition).id)
                    intent.putExtra("cameFrom","past")
                    context.startActivity(intent)
                }

                R.id.btnReOrder ->{ orderViewModel.reorderApi(context,prefs.jwtToken!!,""+list.get(adapterPosition).id) }

                R.id.btnRate ->{
                    val intent = Intent(context, RatingActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(ParamEnum.ORDER_ID.theValue() as String,""+list.get(adapterPosition).id)
                    context.startActivity(intent)
                }

                //Upcoming Order
                R.id.cvUpcoming ->{
                    val intent = Intent(context, OrderDetailActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    intent.putExtra(ParamEnum.ORDER_ID.theValue() as String,""+list.get(adapterPosition).id)
                    intent.putExtra("cameFrom","upcoming")
                    context.startActivity(intent)
                }

            }
        }

    }

}