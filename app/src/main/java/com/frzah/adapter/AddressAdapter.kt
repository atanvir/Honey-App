package com.frzah.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.activity.AddNewAddress.AddNewAddressActivity
import com.frzah.activity.DeliveryAddress.DeliveryAddressActivity
import com.frzah.model.response.success.ResponseBean
import kotlinx.android.synthetic.main.adapter_address.view.*


class AddressAdapter(var context :Context,var list:List<ResponseBean>,var listner: setOnAddressClickListner) : RecyclerView.Adapter<AddressAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_address,parent,false))
    override fun getItemCount(): Int = list.size
    override fun getItemId(position: Int): Long = position.toLong()
    override fun getItemViewType(position: Int): Int =position

    override fun onBindViewHolder(holder: AddressAdapter.MyViewHolder, position: Int) {
        if (list.get(position).type.equals("home")) {
            holder.itemView.ivIcon.setImageResource(R.drawable.home_icn)
            holder.itemView.tvName.text=context.getString(R.string.home)
        }
        else {
            holder.itemView.ivIcon.setImageResource(R.drawable.office_icn)
            holder.itemView.tvName.text = context.getString(R.string.office_commercial)
        }
        holder.itemView.tvPhoneNumber.setText(list.get(position).phone)
        holder.itemView.tvAddress.setText("" + list.get(position).address)
    }

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener,
        PopupMenu.OnMenuItemClickListener {
        init {
            itemView.ivDots.setOnClickListener(this)
            itemView.clAddress.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            when(v!!.id)
            {
                R.id.ivDots ->{
                    val popup = PopupMenu(context, v)
                    popup.inflate(R.menu.options_menu)
                    popup.setOnMenuItemClickListener(this)
                    popup.show()
                }

                R.id.clAddress ->{
                    val intent= Intent(context,AddNewAddressActivity::class.java)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("cameFrom",DeliveryAddressActivity::class.simpleName)
                    intent.putExtra("id",""+list!!.get(adapterPosition).id)
                    intent.putExtra("name",""+list!!.get(adapterPosition).name)
                    intent.putExtra("phone",""+list!!.get(adapterPosition).phone)
                    intent.putExtra("address",""+list!!.get(adapterPosition).address)
                    intent.putExtra("street",""+list!!.get(adapterPosition).street)
                    intent.putExtra("city",""+list!!.get(adapterPosition).city)
                    intent.putExtra("state",""+list!!.get(adapterPosition).state)
                    intent.putExtra("type",""+list!!.get(adapterPosition).type)
                    intent.putExtra("remark",""+list!!.get(adapterPosition).remark)
                    intent.putExtra("country",""+list!!.get(adapterPosition).country)
                    intent.putExtra("latitude",""+list!!.get(adapterPosition).latitude)
                    intent.putExtra("longitude",""+list!!.get(adapterPosition).longitude)
                    intent.putExtra("phone_code",""+list!!.get(adapterPosition).phone_code)
                    intent.putExtra("pin_code",""+list!!.get(adapterPosition).pin_code)
                    context.startActivity(intent)
                }
            }
        }


        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item!!.itemId){
                R.id.remove ->{
                    listner.onRemove(adapterPosition,""+list.get(adapterPosition).id)
                    return true
                }

            }
            return false
        }
    }

     interface setOnAddressClickListner{
        fun onRemove(pos:Int,address_id:String)
    }

}
