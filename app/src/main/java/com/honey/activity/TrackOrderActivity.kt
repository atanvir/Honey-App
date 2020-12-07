package com.honey.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.honey.R
import com.honey.activity.Main.MainActivity
import com.honey.adapter.OrderStatusAdapter
import com.honey.base.BaseActivity
import com.honey.utils.CommonUtils
import kotlinx.android.synthetic.main.activity_track_order.*


class TrackOrderActivity : BaseActivity(), OnMapReadyCallback, GoogleMap.OnCameraMoveListener,
    View.OnClickListener {

    private var mMap: GoogleMap? = null
    private var marker : Marker?=null
    private var dialog : BottomSheetDialog?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track_order)
        init()
        initControl()

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"Order Tracking")
    }

    override fun init() {

    }

    override fun initControl() {
    }

    override fun myObserver() {

    }

    fun showCancelDialog() {
        dialog = BottomSheetDialog (this)
        dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.setContentView(R.layout.dialog_order_cancel)
        dialog!!.getWindow()!!.findViewById<View>(R.id.design_bottom_sheet).setBackgroundResource(android.R.color.transparent)
        val btnCancel=dialog!!.findViewById<Button>(R.id.btnCancel);
        val tvSkip=dialog!!.findViewById<TextView>(R.id.tvSkip);
        btnCancel!!.setOnClickListener(this)
        tvSkip!!.setOnClickListener(this)
        dialog!!.setCancelable(false)
        dialog!!.show()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        val sydney = LatLng(-33.852, 151.211)
        mMap?.apply {
            marker=addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").icon(BitmapDescriptorFactory.fromResource(R.drawable.location)))
            mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            mMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15f))

        }

        mMap!!.setOnCameraMoveListener(this)
    }

    override fun onCameraMove() {
        marker!!.setPosition(mMap!!.cameraPosition.target)
    }

    override fun onClick(p0: View?) {
        when(p0!!.id)
        {
            R.id.btnCancel ->{
                dialog!!.dismiss()
                finish()
            }

            R.id.tvSkip ->{
                dialog!!.dismiss()
                finish()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        if(intent.getStringExtra("cameFrom").ConfirmOrderAdapter::class.simpleName)){
//            finish()
//        }else {
//            var intent = Intent(this, MainActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//            startActivity(intent)
//        }
    }


}