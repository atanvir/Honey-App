package com.frzah.activity.Search

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import com.frzah.R
import com.frzah.activity.Filter.FilterActivity
import com.frzah.adapter.FrgamentPagerAdapter
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.ParamEnum
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_home.ivFilter

class SearchActivity : BaseActivity(), View.OnClickListener, TabLayout.OnTabSelectedListener, TextWatcher {
    companion object{
        var listner:setOnSearchKeyListner?=null
        fun setOnSearchKeyListner(listner: setOnSearchKeyListner){
            this.listner=listner
        }
    }

    val FILTER_RESULT_REQ:Int=10
    var type: String?=""
    var sort_by: String?=""
    var price_low: String?=""
    var price_high: String?=""
    var rating: String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
        initControl()
        myObserver()
    }
    
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this,getString(R.string.search_honey))
    }

    override fun init() {
        val pagerAdapter = FrgamentPagerAdapter(this,"Search", this.supportFragmentManager)
        viewPager.adapter=pagerAdapter
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun initControl() {
        ivFilter.setOnClickListener(this)
        editText.addTextChangedListener(this)
        tabLayout.addOnTabSelectedListener(this)
    }

    override fun myObserver() {
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
         R.id.ivFilter -> {
             val intent=Intent(this,FilterActivity::class.java)
             intent.putExtra(""+ParamEnum.TYPE.theValue(),""+type)
             intent.putExtra(""+ParamEnum.SORT_BY.theValue(),""+sort_by)
             intent.putExtra(""+ParamEnum.PRICE_LOW.theValue(),""+price_low)
             intent.putExtra(""+ParamEnum.PRICE_HIGH.theValue(),""+price_high)
             intent.putExtra(""+ParamEnum.RATING.theValue(),""+rating)
             startActivityForResult(intent,FILTER_RESULT_REQ)
         }
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if(tab?.position==0) editText.setHint(getString(R.string.honey_name))
        else editText.setHint(getString(R.string.store_name))
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
      listner!!.search(s.toString(),type!!,sort_by!!,price_low!!,price_high!!,rating!!)
    }

    interface setOnSearchKeyListner{
        fun search(search_key:String,type:String,sort_by:String,price_low:String,price_high:String,rating:String)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            FILTER_RESULT_REQ ->{
                if(resultCode== Activity.RESULT_OK || resultCode==Activity.RESULT_CANCELED) {
                    if(data!=null) {
                        type = data!!.getStringExtra("" + ParamEnum.TYPE.theValue())
                        sort_by = data!!.getStringExtra("" + ParamEnum.SORT_BY.theValue())
                        price_low = data!!.getStringExtra("" + ParamEnum.PRICE_LOW.theValue())
                        price_high = data!!.getStringExtra("" + ParamEnum.PRICE_HIGH.theValue())
                        rating = data!!.getStringExtra("" + ParamEnum.RATING.theValue())
                        listner!!.search(editText.text.toString(), type!!, sort_by!!, price_low!!, price_high!!, rating!!)
                    }
                }
            }
        }
    }


}