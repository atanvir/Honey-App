package com.frzah.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.frzah.R
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.SharedPreferenceUtil
import kotlinx.android.synthetic.main.adapter_faq.view.*

class FAQAdapter(var context: Context,var list:List<ResponseBean>) : RecyclerView.Adapter<FAQAdapter.MyViewHolder>(){
    var prefs=SharedPreferenceUtil.getInstance(context)
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
            if(prefs.selectedLanguage.equals("en")) itemView.headerText.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(context,R.drawable.up), null)
            else itemView.headerText.setCompoundDrawablesWithIntrinsicBounds( ContextCompat.getDrawable(context,R.drawable.up), null,null ,null)
            itemView.desText.setVisibility(View.VISIBLE)
            }
            else {
            if(prefs.selectedLanguage.equals("en")) itemView.headerText.setCompoundDrawablesWithIntrinsicBounds(null, null,ContextCompat.getDrawable(context,R.drawable.down), null)
            else itemView.headerText.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context,R.drawable.down), null,null, null)
            itemView.desText.setVisibility(View.GONE)
            }
            }
            }
        }
    }

}