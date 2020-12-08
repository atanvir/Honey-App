package com.honey.activity.ContactUs

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.honey.BuildConfig
import com.honey.R
import com.honey.base.BaseActivity
import com.honey.model.request.CommonModel
import com.honey.model.request.ContactUsModel
import com.honey.model.request.UserProfileModel
import com.honey.utils.*
import com.honey.utils.ViewExtension.observeOnce
import kotlinx.android.synthetic.main.activity_contact_us.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class ContactUsActivity : BaseActivity(), View.OnClickListener {
    private lateinit var contactUsViewModel: ContactUsViewModel
    private val IMAGE_REQ=112
    private val PERMISSION=12
    private var path:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"Contact Us")
    }

    override fun init() {
        contactUsViewModel=ViewModelProviders.of(this).get(ContactUsViewModel::class.java)
    }

    override fun initControl() {
        btnSubmit.setOnClickListener(this)
        clAddImage.setOnClickListener(this)
        ivQueryPhoto.setOnClickListener(this)
    }

    override fun myObserver() {
        contactUsViewModel.response.observe(this, Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) checkData(it)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })
        contactUsViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun checkData(it: CommonModel?) {
        finish()
        Toast.makeText(this,it!!.message,Toast.LENGTH_LONG).show()
    }

    override fun onClick(v: View?) {
        when(v!!.id)
        {
            R.id.clAddImage ->{
            if (checkPermissions()) {
                launchIntent()
            }
            }
            R.id.btnSubmit ->{
            if(checkValidations()){
                contactUsViewModel.contactUsApi(this,AddRequestBody(ContactUsModel(edReason.text.toString().trim(),edQuery.text.toString())).body,getUploadedPhoto()!!)
            }
            }
            R.id.ivQueryPhoto ->{clAddImage.performClick()}
        }
    }

    private fun getUploadedPhoto() :MultipartBody.Part?{
        if(path!=null) {
            val imgFile = File(path!!)
            val imagesBody = imgFile.asRequestBody("image/*".toMediaTypeOrNull())
            val imgpart = MultipartBody.Part.createFormData("image", imgFile.name, imagesBody)
            return imgpart
        }
        return null
    }

    private fun checkValidations(): Boolean {
        var ret=true
        if(edReason.text.toString().trim().length==0){
            ret=false
            CommonUtils.showSnackBar(this,"Please specify reason")
        }
        else if(edQuery.text.toString().trim().length==0){
            ret=false
            CommonUtils.showSnackBar(this,"Please specify query")
        }
        else if(path==null){
            ret=false
            CommonUtils.showSnackBar(this,"Please upload photo")
        }
        return ret
    }


    private fun checkPermissions(): Boolean {
        var ret = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ){
                ret = false
                requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),PERMISSION)
            }
        }
        return ret
    }

    private fun launchIntent() {
        val capture_dir: String = Environment.getExternalStorageDirectory().toString() + "/Honey Application/Images/"
        val file = File(capture_dir)
        if (!file.exists()) {
            file.mkdirs()
        }
        path = capture_dir + System.currentTimeMillis() + ".jpg"
        val imageFileUri = FileProvider.getUriForFile(applicationContext, BuildConfig.APPLICATION_ID + ".provider", File(path!!))
        val intent = CommonUtils.getPickIntent(this, imageFileUri)
        startActivityForResult(intent, IMAGE_REQ)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            PERMISSION ->{
                var reqestDecline=false
                for (i in 0..grantResults.size-1) {
                if(grantResults.get(i)==PackageManager.PERMISSION_DENIED)
                {
                    reqestDecline=true
                }
                if(reqestDecline) CommonUtils.showSnackBar(this,"Please allow the permission for the security purpose")
                else launchIntent()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            IMAGE_REQ ->{
            if(resultCode== Activity.RESULT_OK) {
               clAddImage.visibility=View.GONE
               ivQueryPhoto.visibility=View.VISIBLE
               if (data != null) {
                    path = FilePath.getPath(this, Uri.parse(data.getDataString()))
                    path = path
                    CommonUtils.setRoundImage(this, ivQueryPhoto, null, "" + Uri.parse(data.getDataString()))
                }
               else {
                    CommonUtils.setRoundImage(this, ivQueryPhoto, null, "" + Uri.parse(path))
                }
            }
            }
        }
    }

}