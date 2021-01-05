package com.frzah.activity.Rating

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import kotlinx.android.synthetic.main.activity_rating.*

class RatingActivity : BaseActivity(), View.OnClickListener {
    private lateinit var ratingViewModel: RatingViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setToolbar(this,getString(R.string.add_review))
    }

    override fun init() {
        ratingViewModel=ViewModelProviders.of(this).get(RatingViewModel::class.java)
    }

    override fun initControl() {
        btnSubmit.setOnClickListener(this)
    }

    override fun myObserver() {
        ratingViewModel.response.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) {
            finish()
            Toast.makeText(this,it.message,Toast.LENGTH_LONG).show()
            }
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) {showSnackBar(this,it.message)} })

        ratingViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })

    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
         R.id.btnSubmit ->{
             if(checkValidation()) {
                 ratingViewModel.orderRatingApi(this, prefs.jwtToken!!,
                     intent!!.getStringExtra(ParamEnum.ORDER_ID.theValue() as String)!!,
                     ratingBar.rating.toString(),
                     rbStore.rating.toString(),
                     edSuggestion.text.toString()
                 )
             }
         }
        }
    }

    private fun checkValidation(): Boolean {
        var ret=true
        if(ratingBar.rating.toString().equals("0.0") && rbStore.rating.toString().equals("0.0") && edSuggestion.text.toString().equals("")){
            ret=false
            showSnackBar(this,getString(R.string.please_give_rating_first))
        }else if(edSuggestion.text.toString().equals("")){
            ret=false
            showSnackBar(this,getString(R.string.please_advice_better_suggestion))
        }

        return ret
    }
}