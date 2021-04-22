package com.frzah.activity.AddNewAddress

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.frzah.R
import com.frzah.activity.DeliveryAddress.DeliveryAddressActivity
import com.frzah.activity.Payment.PaymentActivity
import com.frzah.activity.SearchLocation.SearchLocationActivity
import com.frzah.activity.SelectLocation.SelectLocationActivity
import com.frzah.base.BaseActivity
import com.frzah.model.request.AddressModel
import com.frzah.model.request.CommonModel
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.CommonUtils.Companion.showSnackBar
import com.frzah.utils.ErrorUtil
import com.frzah.utils.ParamEnum
import kotlinx.android.synthetic.main.activity_add_new_address.*

class AddNewAddressActivity : BaseActivity(), View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    val ADDRESS_REQ_CODE: Int =12
    var addressType: String?=null
    var remark: String="2"
    var address_id: String=""
    var latitude:Double?=null
    var longitude:Double?=null
    var country:String?=null
    private lateinit var addNewViewModel:AddNewAddressViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_address)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        if(intent.getStringExtra("id")!=null) setToolbar(this,getString(R.string.update_addres))
        else setToolbar(this,getString(R.string.add_new_address))
    }

    override fun init() {
        countryCodePicker.setAutoDetectedCountry(true)
        if(intent.getStringExtra("id")!=null)
        {
            edFullName.setText(getValue("name"))
            edPhoneNumber.setText(getValue("phone"))
            edAddress.setText(getValue("address"))
            edStreet.setText(getValue("street"))
            edCity.setText(getValue("city"))
            edState.setText(getValue("state"))
            edPinCode.setText(getValue("pin_code"))
            addressType=getValue("type")
            remark=getValue("remark")
            country= getValue("country")
            latitude=getValue("latitude").toDouble()
            longitude=getValue("longitude").toDouble()
            address_id=getValue("id")
            if(!getValue("phone_code")!!.equals("null")) {
                countryCodePicker.setCountryForPhoneCode(getValue("phone_code").toInt())
            }
            countryCodePicker.setAutoDetectedCountry(true)
            if(remark.equals("1"))
            {
                cbDefaultAddress.isChecked=true
            }
            if(addressType.equals("home")) rbHome.isChecked=true
            else rbOffice.isChecked=false

        }
        else if(intent.getStringExtra("cameFrom").equals(SearchLocationActivity::class.simpleName))
        {
            edAddress.setText(intent!!.getStringExtra(ParamEnum.ADDRESS.theValue().toString()))
            edState.setText(intent!!.getStringExtra(ParamEnum.STATE.theValue().toString()))
            edCity.setText(intent!!.getStringExtra(ParamEnum.CITY.theValue().toString()))
            edPinCode.setText(intent!!.getStringExtra(ParamEnum.PIN_CODE.theValue().toString()))
            longitude=intent.getDoubleExtra(ParamEnum.LONGITUDE.theValue().toString(),0.0)
            latitude=intent.getDoubleExtra(ParamEnum.LATITUDE.theValue().toString(),0.0)
            country=intent.getStringExtra(ParamEnum.COUNTRY.theValue().toString())
        }
        addNewViewModel= ViewModelProvider(this).get(AddNewAddressViewModel::class.java)
    }

    override fun initControl() {
        btnSave.setOnClickListener(this)
        edAddress.setOnClickListener(this)
        rgAddress.setOnCheckedChangeListener(this)
    }

    fun getValue(key:String) : String{
        return ""+intent.getStringExtra(key)
    }

    override fun myObserver() {
        addNewViewModel.response.observe(this, Observer {
        if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
        else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })

        addNewViewModel.editResponse.observe(this, Observer {
            if(it.status!!.equals(ParamEnum.SUCCESS.theValue())) setDataToUI(it)
            else if(it.status.equals(ParamEnum.FAILURE.theValue())) showSnackBar(this,it.message) })

        addNewViewModel.error.observe(this, Observer{ ErrorUtil.handlerGeneralError(this, it) })
    }

    private fun setDataToUI(it: CommonModel?) {
        if(intent.getStringExtra("cameFrom").equals(AddNewAddressActivity::class.simpleName) ||
           intent.getStringExtra("cameFrom").equals(SearchLocationActivity::class.simpleName))
        {
            finish()
            val intent=Intent(this,SearchLocationActivity::class.java)
            startActivity(intent)
        }else if(intent.getStringExtra("cameFrom").equals(DeliveryAddressActivity::class.simpleName))
        {
            finish()
            val intent=Intent(this,DeliveryAddressActivity::class.java)
            startActivity(intent)
        }else if(intent.getStringExtra("cameFrom").equals(PaymentActivity::class.simpleName))
        {
            finish()
            val intent=Intent(this,PaymentActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onClick(p0: View?) {

        when(p0!!.id)
        {
            R.id.btnSave ->
            { if(checkValidation()) {
                if (cbDefaultAddress.isChecked) remark = "1"
                else remark="2"
                if (intent.getStringExtra("id") != null) { addNewViewModel.userEditAddressApi(this, AddressModel(address_id, prefs.jwtToken!!, edFullName.text.toString(), prefs.phone!!, edAddress.text.toString(), edStreet.text.toString(), edCity.text.toString(), edState.text.toString(), addressType!!, remark, country!!, latitude!!, longitude!!, countryCodePicker.selectedCountryCodeWithPlus, edPinCode.text.toString())) }
                else { addNewViewModel.userAddAddressApi(this, AddressModel(address_id, prefs.jwtToken!!, edFullName.text.toString(), prefs.phone!!, edAddress.text.toString(), edStreet.text.toString(), edCity.text.toString(), edState.text.toString(), addressType!!, remark, country!!, latitude!!, longitude!!, countryCodePicker.selectedCountryCodeWithPlus, edPinCode.text.toString())) }
            }
            }

            R.id.edAddress ->{
                val intent=Intent(this, SelectLocationActivity::class.java)
                intent.putExtra("cameFrom", AddNewAddressActivity::class.simpleName)
                startActivityForResult(intent,ADDRESS_REQ_CODE)
            }

        }
    }

    private fun checkValidation(): Boolean {
        var ret=true
        if(edFullName.text.toString().length==0) {
            ret=false
            showSnackBar(this,getString(R.string.please_enter_full_name))
        }
       /* else if(edPhoneNumber.text.toString().length==0 || edPhoneNumber.text.toString().length<9) {
            ret=false
            if(edPhoneNumber.text.toString().length==0) showSnackBar(this,getString(R.string.please_enter_phone_number))
            else if(edPhoneNumber.text.toString().length<9) showSnackBar(this,getString(R.string.please_enter_valid_phone_number))
        }*/
        else if(edAddress.text.toString().length==0) {
            ret=false
            showSnackBar(this,getString(R.string.please_select_address))
        }
        else if(addressType==null){
            ret=false
            showSnackBar(this,getString(R.string.please_select_type_of_address))
        }
        return ret
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("requestCode",""+requestCode)

        when(requestCode) {

            ADDRESS_REQ_CODE -> {
                if(resultCode== Activity.RESULT_OK)
                {
                    intent.putExtra("cameFrom",data!!.getStringExtra("cameFrom"))
                    edAddress.setText(data!!.getStringExtra(ParamEnum.ADDRESS.theValue().toString()))
                    edState.setText(data!!.getStringExtra(ParamEnum.STATE.theValue().toString()))
                    edCity.setText(data!!.getStringExtra(ParamEnum.CITY.theValue().toString()))
                    edPinCode.setText(data!!.getStringExtra(ParamEnum.PIN_CODE.theValue().toString()))
                    longitude=data.getDoubleExtra(ParamEnum.LONGITUDE.theValue().toString(),0.0)
                    latitude=data.getDoubleExtra(ParamEnum.LATITUDE.theValue().toString(),0.0)
                    country=data.getStringExtra(ParamEnum.COUNTRY.theValue().toString())

                }else if(resultCode==Activity.RESULT_CANCELED)
                {

                }
            }
        }
    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when(group!!.checkedRadioButtonId)
        {
            R.id.rbHome ->{ addressType=ParamEnum.HOME.theValue().toString() }
            R.id.rbOffice ->{ addressType=ParamEnum.OFFICE.theValue().toString() }
        }
    }
}

