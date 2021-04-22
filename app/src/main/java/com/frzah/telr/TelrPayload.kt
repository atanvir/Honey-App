package com.frzah.telr


import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.frzah.activity.Main.MainActivity
import com.frzah.model.response.success.ResponseBean
import com.frzah.utils.ParamEnum
import com.frzah.utils.SharedPreferenceUtil
import com.frzah.utils.ViewExtension.TAG
import com.telr.mobile.sdk.entity.request.payment.*
import java.math.BigInteger
import java.util.*

object  TelrPayload {


     fun getTelrPayoad(context: Context, phoneNo:String, defaultAddress: ResponseBean?, totalAmount:String): MobileRequest? {
        val prefs=SharedPreferenceUtil.getInstance(context);
        val mobile = MobileRequest()
        mobile.store = ParamEnum.TELR_STORE_ID.theValue() as String
        mobile.key = ParamEnum.TELR_API_KEY.theValue() as String
        mobile.app = getApplicationDetail(context,prefs)
        mobile.tran = getTransactionDetail(context,totalAmount,prefs)
        mobile.billing = getBillingDetail(prefs,phoneNo,defaultAddress)
        mobile.device = getDeviceDetail(context)
        return mobile
    }

    private fun getDeviceDetail(context: Context): Device? {
        val device=Device()
        device.id=Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
        device.type="Android"
        return device

    }
    private fun getApplicationDetail(context: Context, prefs:SharedPreferenceUtil): App? {
        val app = App()
        app.id =context.packageName
        app.name =context.applicationInfo.loadLabel(context.packageManager).toString()
        app.user = prefs.id
        System.out.println(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName)
        app.version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            app.sdk = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).longVersionCode.toString()
        }
        return app

    }
    private fun getTransactionDetail(context: Context,amount:String,prefs: SharedPreferenceUtil): Tran? {
        val tran = Tran()
        tran.test = "0" // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.type = "auth"
        /*
        Transaction type
        'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
        'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
        'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
         */
        tran.clazz = "paypage" // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.cartid = BigInteger(128, Random()).toString()
        if(prefs.soldBy.equals("")) tran.description = " " // Transaction description
        else tran.description=prefs.soldBy
        tran.currency = "SAR"
        tran.amount = amount
        tran.version= context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName
        tran.language = prefs.selectedLanguage
        return tran

    }
    private fun getBillingDetail(prefs: SharedPreferenceUtil,phoneNo:String, defaultAddress: ResponseBean?): Billing? {
        val billing = Billing()
        billing.address = getBillingAddressDetail(defaultAddress)
        billing.name = getBillingName(prefs)
        billing.email = ParamEnum.TELR_EMAIL.theValue() as String
        billing.setPhone(phoneNo)
        return billing

    }
    private fun getBillingAddressDetail(defaultAddress: ResponseBean?): Address? {
        val address = Address()
        address.city = defaultAddress?.city
        val country=defaultAddress?.address?.split(",")
        Log.e(TAG(this),"-->"+country)
        address.country = /*country!![country.size-1]*/"Saudi Arabia"
        address.region = defaultAddress?.state
        address.zip=defaultAddress?.pin_code
        address.line1 = defaultAddress?.address
        return  address
    }
    private fun getBillingName(prefs: SharedPreferenceUtil): Name? {
        val name = Name()
        name.title = "Mr"
        Log.e(TAG(this),""+prefs.name)
        if(prefs.name.equals("")){
            name.first =" "
            name.last = "  "
        }else {
            if (prefs.name!!.trim().contains(" ")) {
                name.first = prefs.name!!.split(" ")[0]
                name.last = prefs.name!!.split(" ")[1]

            } else {

                name.first = prefs.name
                name.last = " jujh "
            }
        }
        return name
    }
}