package com.honey.fragment.FavAndSearchTab

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.honey.R
import com.honey.activity.Search.SearchActivity
import com.honey.adapter.CommonTabAdapter
import com.honey.base.BaseFragment
import com.honey.model.request.CommonModel
import com.honey.model.response.success.ProductDetailModel
import com.honey.utils.CommonUtils
import com.honey.utils.CommonUtils.Companion.showLoadingDialog
import com.honey.utils.CommonUtils.Companion.showSnackBar
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.observeOnce
import kotlinx.android.synthetic.main.fragment_common_tab.*

class CommonTabFragment(var pos : Int,var screen: String) : BaseFragment(), CommonTabAdapter.setOnUnFavClickListner, SearchActivity.setOnSearchKeyListner {
    private lateinit var commonTabViewModel: CommonTabViewModel
    private var dataList:MutableList<ProductDetailModel>?=ArrayList()
    var position:Int=0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_common_tab,container,false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
        initControl()
        myObserver()
    }
    override fun init() {
        commonTabViewModel=ViewModelProviders.of(requireActivity()).get(CommonTabViewModel::class.java)
        if(screen.equals("Search"))
        {
            showLoadingDialog(requireActivity())
            SearchActivity.setOnSearchKeyListner(this)
            if(pos==0) commonTabViewModel.searchApi(prefs.jwtToken!!,"","","","","","")
            else commonTabViewModel.searchApi(prefs.jwtToken!!,"","","","","","")

        }else{
            if(pos==0) commonTabViewModel.favListApi(requireActivity(),prefs.jwtToken!!,""+ParamEnum.PRODUCT.theValue())
            else commonTabViewModel.favListApi(requireActivity(),prefs.jwtToken!!,""+ParamEnum.STORE.theValue())
        }

    }

    override fun initControl() {
    }

    override fun myObserver() {
        commonTabViewModel.response.observeOnce(requireActivity(), Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it!!)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(requireActivity(),it.message) })

        commonTabViewModel.onSearchResponse.observe(requireActivity(), Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUi(it!!)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(requireActivity(),it.message) })

        commonTabViewModel.onFavResponse.observeOnce(requireActivity(), Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) removeData(it!!)

        else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(requireActivity(),it.message) })
        commonTabViewModel.error.observeOnce(requireActivity(), Observer{ ErrorUtil.handlerGeneralError(requireActivity(), it) })
    }

    private fun removeData(response: CommonModel) {
        recycleView.adapter!!.notifyDataSetChanged()
    }

    private fun setDataToUi(it: CommonModel) {
        val staggeredManger = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        val linearManger = LinearLayoutManager(requireContext())
        if(pos==0) recycleView.layoutManager=linearManger
        else recycleView.layoutManager=staggeredManger
        dataList!!.clear()

        if(it.wishlistproduct==null)
        {
            if (pos == 0) {
                dataList = it.response!!.productList!!
                if(!(dataList!!.size>0)) lottieAnim.visibility=View.VISIBLE
                else lottieAnim.visibility=View.GONE
            } else {
                if (it.response!!.storeList!!.size > 0 && dataList!!.size==0) {
                    dataList!!.clear()
                    dataList = it.response!!.storeList!!
                    dataList!!.add(0, dataList!!.get(0))
                }
                if(!(dataList!!.size>0)) lottieAnim.visibility=View.VISIBLE
                else lottieAnim.visibility=View.GONE
            }

        }else {
            if (pos == 0) {
                dataList = it.wishlistproduct!!.productList!!
                if(!(dataList!!.size>0)) lottieAnim.visibility=View.VISIBLE
                else lottieAnim.visibility=View.GONE
            } else {
                dataList!!.clear()
                if (it.wishlistproduct!!.storeList!!.size > 0 && dataList!!.size == 0) {
                    dataList!!.clear()
                    dataList = it.wishlistproduct!!.storeList!!
                    dataList!!.add(0, dataList!!.get(0))
                }

                if (!(dataList!!.size > 0)) lottieAnim.visibility = View.VISIBLE
                else lottieAnim.visibility = View.GONE
            }
        }
        recycleView.adapter = CommonTabAdapter(requireContext(), screen, pos, dataList!!,this)
        recycleView.notifyDataSetChanged()
        recycleView.scheduleLayoutAnimation()
    }

    override fun onFav(pos: Int,position: Int, _id: String, type: String) {
        commonTabViewModel.addtowishApi(requireActivity(),prefs.jwtToken!!,_id,type)
    }



    override fun search(search_key: String, type: String, sort_by: String, price_low: String, price_high: String,rating:String) {
        if(pos==0) commonTabViewModel.searchApi(prefs.jwtToken!!,search_key,type,sort_by,price_low,price_high,rating)
        else commonTabViewModel.searchApi(prefs.jwtToken!!,search_key,type,sort_by,price_low,price_high,rating)}
}