package com.honey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.honey.R
import kotlinx.android.synthetic.main.adapter_payment_method.view.*

class PaymentMethodAdapter(var context: Context) : RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder>(){

    val images= arrayListOf(R.drawable.mastercard,R.drawable.paypal,R.drawable.cod)
    var selectedpos=0;
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentMethodAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_payment_method,parent,false))
    }

    override fun getItemCount(): Int {
        return images.size
    }

    override fun onBindViewHolder(holder: PaymentMethodAdapter.MyViewHolder, position: Int) {
        holder.itemView.ivSymobl.setImageResource(images.get(position))
        holder.itemView.ivSymobl.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                selectedpos=position
                notifyDataSetChanged()

            }
        })

        if(selectedpos==position) holder.itemView.mainCl.background=ContextCompat.getDrawable(context,R.drawable.drawable_yellow_stroke)
        else holder.itemView.mainCl.background=ContextCompat.getDrawable(context,R.drawable.drawable_grey_stroke)
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

}