package com.frzah.activity.Review

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
import com.frzah.R
import com.frzah.adapter.ReviewAdapter
import com.frzah.base.BaseActivity
import com.frzah.model.request.ReviewModel
import com.frzah.utils.CommonUtils.Companion.setRoundImage
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
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
        setToolbar(this,getString(R.string.reviews))
        setRoundImage(this,ciPic,lottieAnim,prefs.image!!)
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
            else if(it.status!!.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })
        reviewViewModel.reviewResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) {
                finish()
                Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) { showSnackBar(this,it.message)} })
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