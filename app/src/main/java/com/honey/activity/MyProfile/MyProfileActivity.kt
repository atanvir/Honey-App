package com.honey.activity.MyProfile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.honey.BuildConfig
import com.honey.R
import com.honey.base.BaseActivity
import com.honey.model.request.CommonModel
import com.honey.model.request.UserProfileModel
import com.honey.utils.*
import com.honey.utils.ViewExtension.observeOnce
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_my_profile.countryCodePicker
import kotlinx.android.synthetic.main.activity_my_profile.edPhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MyProfileActivity : BaseActivity(), View.OnClickListener {
    private lateinit var profileViewModel: MyProfileViewModel
    private var path:String?=null
    private val IMAGE_REQ=112
    private val PERMISSION=12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        CommonUtils.setToolbar(this,"")
    }

    override fun init() {
        profileViewModel=ViewModelProviders.of(this).get(MyProfileViewModel::class.java)
        profileViewModel.userProfileApi(this,prefs.jwtToken!!)
        countryCodePicker.setAutoDetectedCountry(true)
    }

    override fun initControl() {
        btnSave.setOnClickListener(this)
        ivChangeProfile.setOnClickListener(this)
    }

    override fun myObserver() {
        profileViewModel.response.observe(this, Observer {   if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
        else if(it.status!!.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })
        profileViewModel.editProfileResponse.observe(this, Observer {   if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) saveData(it)
        else if(it.status!!.equals(ParamEnum.FAILURE.theValue())) CommonUtils.showSnackBar(this,it.message) })
        profileViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun saveData(it: CommonModel?) {
        Toast.makeText(this,it!!.message,Toast.LENGTH_LONG).show()
        if(it.user_profile!!.image!=null)
        {
            prefs.image=it.user_profile!!.image
            prefs.name=it.user_profile!!.name
        }
        finish()
    }

    private fun setDataToUI(it: CommonModel) {
        CommonUtils.setNormalImage(this,ivProfile,lvProfile,it.user_profile!!.image)

        if(it.user_profile.name!=null) {
            tvName.setText("" + it.user_profile.name)
            edFullName.setText("" + it.user_profile.name)
        }

        if(it.user_profile.phone!= null) {
            edPhoneNumber.setText("" + it.user_profile.phone!!)
        }

        countryCodePicker.setAutoDetectedCountry(true)
        countryCodePicker.setCountryForPhoneCode(it.user_profile.phoneCode!!.toInt())
    }

    override fun onClick(v: View?) {
        when (v!!.id) {

            R.id.btnSave -> {
            if (checkValidation()) {
                profileViewModel.userEditProfileApi(this, getProfilePic(), AddRequestBody(UserProfileModel(prefs.jwtToken!!, edPhoneNumber.text.toString(),edFullName.text.toString(), "")).body)
            }
            }

            R.id.ivChangeProfile -> {
            if (checkPermissions()) {
                launchIntent()
            }

            }
        }
    }

    private  fun getProfilePic(): MultipartBody.Part? {
        if(path!=null) {
            val imgFile = File(path!!)
            val imagesBody = Compressor(this).compressToFile(imgFile).asRequestBody("image/*".toMediaTypeOrNull())
            val imgpart = MultipartBody.Part.createFormData("image", imgFile.name, imagesBody)
            return imgpart
        }
        return null
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

    private fun checkValidation(): Boolean {
        var ret=true
        if( edPhoneNumber.text.toString().length<9)
        {
            ret=false
            if(edPhoneNumber.text.toString().length<9) CommonUtils.showSnackBar(this,"Please enter valid phone number")
        }
        return ret
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode)
        {
            PERMISSION ->{
                var reqestDecline=false
                for (i in 0..grantResults.size-1) {
                    if (grantResults.get(i) == PackageManager.PERMISSION_DENIED) {
                        reqestDecline = true
                    }
                }
                    
                    if(reqestDecline) CommonUtils.showSnackBar(this,"Please allow the permission for the security purpose")
                    else launchIntent()
            }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            IMAGE_REQ ->{
            if(resultCode== Activity.RESULT_OK) {
                if (data != null) {
                    path = FilePath.getPath(this, Uri.parse(data.getDataString()))
                    path = path
                    CommonUtils.setNormalImage(this, ivProfile, lvProfile, "" + Uri.parse(data.getDataString()))
                } else {
                    CommonUtils.setNormalImage(this, ivProfile, lvProfile, "" + Uri.parse(path))
                }
            }
            }
        }
    }
}