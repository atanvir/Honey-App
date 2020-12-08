package com.honey.activity.Rating

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.honey.R
import com.honey.base.BaseActivity
import com.honey.utils.CommonUtils
import com.honey.utils.ErrorUtil
import com.honey.utils.ParamEnum
import kotlinx.android.synthetic.main.activity_rating.*
import kotlinx.android.synthetic.main.layout_main_toolbar.*

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
        CommonUtils.setToolbar(this,"Add Review")
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
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) {CommonUtils.showSnackBar(this,it.message)} })

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
            CommonUtils.showSnackBar(this,"Please give the rating first")
        }else if(edSuggestion.text.toString().equals("")){
            ret=false
            CommonUtils.showSnackBar(this,"Please advice for better suggestions!")
        }

        return ret
    }
}