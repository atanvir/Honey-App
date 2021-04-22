package com.frzah.fragment.Notification

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.adapter.NotificationAdapter
import com.frzah.base.BaseFragment
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : BaseFragment() {
    private lateinit var notifiactionViewModel: NotifiactionViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
      return inflater.inflate(R.layout.fragment_notification,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initControl()
        myObserver()
    }

    override fun init() {
        notifiactionViewModel= ViewModelProviders.of(this).get(NotifiactionViewModel::class.java)
        notifiactionViewModel.notficationListApi(requireActivity(),prefs.jwtToken!!)
    }

    override fun initControl() {
    }

    override fun myObserver() {
        notifiactionViewModel.response.observe(requireActivity(), Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(requireActivity(),it.message) })
        notifiactionViewModel.error.observe(requireActivity(), Observer{ ErrorUtil.handlerGeneralError(requireActivity(), it) })
    }

    private fun setDataToUI(it: CommonModel?) {
        if(it!!.notficationlist!!.size>0){
            lvNotification.visibility=View.GONE
            val homeAdapter= NotificationAdapter(requireContext(),it.notficationlist!!)
            rvNotification.adapter=homeAdapter
            rvNotification.notifyDataSetChanged()
            rvNotification.scheduleLayoutAnimation()

        }else{
            lvNotification.visibility=View.VISIBLE
        }
    }


}