package com.honey.activity.Review

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.honey.R
import com.honey.adapter.ReviewAdapter
import com.honey.base.BaseActivity
import com.honey.model.request.CommonListModel
import com.honey.model.request.ReviewModel
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import com.honey.utils.ViewExtension.observeOnce
import kotlinx.android.synthetic.main.activity_review.*

class ReviewActivity : BaseActivity(), TextView.OnEditorActionListener {
    private lateinit var reviewViewModel: ReviewViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"Reviews")
        CommonUtils.setRoundImage(this,ciPic,lottieAnim,prefs.image!!)
    }

    override fun init() {
        reviewViewModel=ViewModelProviders.of(this).get(ReviewViewModel::class.java)
        reviewViewModel.allReviewsApi(this,intent.getStringExtra("type")!!,intent.getStringExtra("id")!!)
    }

    override fun initControl() {
        etReview.setOnEditorActionListener(this)
    }

    override fun myObserver() {
        reviewViewModel.response.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
            else if(it.status!!.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })
        reviewViewModel.reviewResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) {
                finish()
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) { CommonUtils.showSnackBar(this,it.message)} })
        reviewViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setDataToUI(it: ReviewModel?) {
        rvReviews.layoutManager=LinearLayoutManager(this)
        rvReviews.adapter=ReviewAdapter(this,it!!.response!!)
        rvReviews.scheduleLayoutAnimation()
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        if(actionId== EditorInfo.IME_ACTION_DONE){
            reviewViewModel.addReviewApi(this,prefs.jwtToken!!,etReview.text.toString(),intent.getStringExtra("type")!!,intent.getStringExtra("id")!!)
        }
        return false
    }
}