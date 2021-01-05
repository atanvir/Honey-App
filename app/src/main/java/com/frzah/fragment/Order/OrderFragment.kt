package com.frzah.fragment.Order

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.frzah.R
import com.frzah.adapter.CommonOrderAdapter
import com.frzah.base.BaseFragment
import com.frzah.model.request.CommonListModel
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.CommonUtils.Companion.showSnackBarGreen
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import com.frzah.utils.ViewExtension.TAG
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment(var pos:Int) : BaseFragment() {
    private lateinit var orderViewModel: OrderViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_order,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initControl()
        myObserver()
    }

    override fun init() {
        orderViewModel=ViewModelProviders.of(this).get(OrderViewModel::class.java)
        if(pos==0) orderViewModel.upcomingOrderApi(requireActivity(),prefs.jwtToken!!)
        else orderViewModel.pastOrdersApi(requireActivity(),prefs.jwtToken!!)
    }

    override fun initControl() {
    }

    override fun myObserver() {
        orderViewModel.upcomingResponse.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) upcomingOrderData(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(requireActivity(),it.message) })

        orderViewModel.reOrderResponse.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) refreshOrder(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(requireActivity(),it.message) })

        orderViewModel.pastResponse.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) pastOrderData(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(requireActivity(),it.message) })

        orderViewModel.error.observe(requireActivity(), Observer{ ErrorUtil.handlerGeneralError(requireActivity(), it) })
    }

    private fun refreshOrder(it: CommonListModel?) {
        Log.e(TAG(this),it!!.message!!)
        showSnackBarGreen(requireActivity(), it.message!!)
        if(pos==0) orderViewModel.upcomingOrderApi(requireActivity(),prefs.jwtToken!!)
        else orderViewModel.pastOrdersApi(requireActivity(),prefs.jwtToken!!)
    }

    private fun upcomingOrderData(it: CommonListModel?) {
        if(it!!.response!!.size>0){
        rvOrders.visibility=View.VISIBLE
        lanim.visibility=View.GONE
        rvOrders.layoutManager=LinearLayoutManager(activity)
        rvOrders.adapter= CommonOrderAdapter(requireContext(),pos, it.response!!,orderViewModel)

        rvOrders.notifyDataSetChanged()
        rvOrders.scheduleLayoutAnimation()
        }else{
            lanim.visibility=View.VISIBLE
            rvOrders.visibility=View.GONE
        }
    }

    private fun pastOrderData(it: CommonListModel?) {
        if(it!!.response!!.size>0) {
            lanim.visibility=View.GONE
            rvOrders.visibility=View.VISIBLE
            rvOrders.layoutManager = LinearLayoutManager(activity)
            rvOrders.adapter = CommonOrderAdapter(requireContext(), pos,it!!.response!!,orderViewModel)
            rvOrders.notifyDataSetChanged()
            rvOrders.scheduleLayoutAnimation()
        }else{
            lanim.visibility=View.VISIBLE
            rvOrders.visibility=View.GONE
        }
    }
}