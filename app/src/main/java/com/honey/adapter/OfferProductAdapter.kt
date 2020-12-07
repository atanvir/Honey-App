package com.honey.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honey.R
import com.honey.activity.OrderDetail.OrderDetailActivity
import com.honey.model.response.success.CartListModel
import com.honey.model.response.success.ResponseBean
import com.honey.utils.CommonUtils
import kotlinx.android.synthetic.main.adapter_bag_item.view.*

class OfferProductAdapter(var context: Context, var list: List<ResponseBean>?): RecyclerView.Adapter<OfferProductAdapter.MyViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferProductAdapter.MyViewHolder=MyViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_product_offer,parent,false))
    override fun getItemCount(): Int=2
    override fun getItemId(position: Int): Long =position.toLong()
    override fun getItemViewType(position: Int):Int=position
    override fun onBindViewHolder(holder: OfferProductAdapter.MyViewHolder, position: Int) {
    }
   inner class  MyViewHolder(viewHolder: View): RecyclerView.ViewHolder(viewHolder), View.OnClickListener {
        init {

        }

        override fun onClick(v: View?) {
            when(v!!.id)
            {

            }
        }
    }

}