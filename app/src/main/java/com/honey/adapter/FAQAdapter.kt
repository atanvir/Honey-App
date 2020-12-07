package com.honey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.honey.R
import com.honey.model.response.success.ResponseBean
import kotlinx.android.synthetic.main.adapter_faq.view.*

class FAQAdapter(var context: Context,var list:List<ResponseBean>) : RecyclerView.Adapter<FAQAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FAQAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context!!).inflate(R.layout.adapter_faq,parent,false))
    override fun getItemCount(): Int= list.size
    override fun onBindViewHolder(holder: FAQAdapter.MyViewHolder, position: Int) {
        holder.itemView.headerText.text=list.get(position).question
        holder.itemView.desText.text=list.get(position).ans
    }

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener {
        init {
            itemView.mainCl.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            when(v!!.id)
            {
                R.id.mainCl ->{
                    if (itemView.desText.visibility == View.GONE) {
                        itemView.headerText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context,R.drawable.up), null)
                        itemView.desText.setVisibility(View.VISIBLE)
                    } else {
                        itemView.headerText.setCompoundDrawablesWithIntrinsicBounds(null, null,ContextCompat.getDrawable(context,R.drawable.down), null)
                        itemView.desText.setVisibility(View.GONE)
                    }
                }
            }
        }
    }

}