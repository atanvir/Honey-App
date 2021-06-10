package com.frzah.utils

import android.app.Activity
import android.content.*
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.frzah.R
import com.frzah.activity.Login.LoginActivity
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.ShopDetail.ShopDetailsActivity
import com.frzah.fragment.Favorite.FavoriteFragment
import com.frzah.fragment.Notification.NotificationFragment
import com.frzah.webservices.ApiConstant
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class CommonUtils {

    companion object {
        /* All Commom Constants */
        const val GETTING_ADDRESS = 1
        const val NOT_SERVE_THIS_AREA = 2
        const val HIDE_INFO_WINDOW = 3
        const val PLACE_REQ_CODE = 4
        const val PERMISSION = 5
        const val PERMISSION_DIALOG_REQ = 6
        const val SHOPS = 1
        const val OFFERS = 2
        const val DELAY_MS :Long = 500
        const val PERIOD_MS: Long = 3000
        const val UPCOMING_ORDER_TAB: Int = 0
        const val PAST_ORDER_TAB: Int = 1
        const val LINE_WISE_READ_MORE: Int = 3
        const val CHARACTER_WISE_READ_MORE: Int = 40


        //Shared Prefrence constants
        const val PREFRENCE_NAME = "com.honey"
        const val PERSISTABLE_PREFRENCE_NAME = "cache_memory"
        const val DELAY_FOR_NEXT_SCREEN: Long = 3500
        const val DELAY_GIF_BACKGROUND: Long = 500

        //Patterns
        const val PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&<>*~:`-]).{8,}\$"
        const val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        var gson: Gson? = null
        var customProgressBar: CustomProgressBar? = null
        var noInternetDailog:NoInternetConnectionDailog? =null

        fun loadFragment(context: Context, activity: FragmentManager, fragment: Fragment) {
            val prefs=SharedPreferenceUtil.getInstance(context)
            if(fragment is FavoriteFragment && prefs.jwtToken!!.equals(""))
            {
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }else if(fragment is NotificationFragment && prefs.jwtToken!!.equals(""))
            {
                val intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }else {
                SwitchFragment.changeFragment(activity, fragment, false, true)
            }
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun setToolbar(activity: AppCompatActivity, title: String)
        {
           val tvTitle =activity.findViewById<TextView>(R.id.tvTitle)
           val ivBack =activity.findViewById<ImageView>(R.id.ivBack)
           val ivFav =activity.findViewById<ImageView>(R.id.ivFav)
           val ivHeart =activity.findViewById<ImageView>(R.id.ivHeart)
           val toolbar =activity.findViewById<Toolbar>(R.id.toolbar)
            tvTitle.visibility= View.VISIBLE
            tvTitle.text=title
            ivBack.setOnClickListener(object : View.OnClickListener {
                override fun onClick(p0: View?) {
                    activity.onBackPressed()
                }
            })

            if(activity is ShopDetailsActivity)
            {
                tvTitle.visibility= View.GONE
                ivFav.visibility=View.VISIBLE
                ivHeart.visibility=View.VISIBLE

            }else if(activity is ShopDetailsActivity)
            {
                tvTitle.visibility= View.GONE
                ivFav.visibility=View.VISIBLE
            }

            activity.setSupportActionBar(toolbar)
        }

        fun startActivity(context: Context, className: Class<out Any?>?) : Intent {
            val intent:Intent
            val prefs=SharedPreferenceUtil.getInstance(context)
            if(context is MainActivity && prefs.jwtToken!!.equals("")){
                intent = Intent(context, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }else {
                intent = Intent(context, className)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.startActivity(intent)
            }
            return intent
        }

        fun networkConnectionCheck(context: Context): Boolean {
            val isConnected = isOnline(context)
            if (!isConnected) {
                 showNoInternetDialog(context as Activity)
            }
            return isConnected
        }

        fun isOnline(context: Context): Boolean {
            return try {
                val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val mobile_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                val wifi_info = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                if (mobile_info != null) {
                    if (mobile_info.isConnectedOrConnecting || wifi_info!!.isConnectedOrConnecting) true
                    else false
                } else {
                    if (wifi_info!!.isConnectedOrConnecting) true
                    else false
                    }
                } catch (e: Exception) {
                e.printStackTrace()
                println("" + e)
                false
            }
       }

       fun showLoadingDialog(activity: Activity?) {
            if (customProgressBar == null) customProgressBar = CustomProgressBar.show(
                activity,
                true
            )
            try {
                customProgressBar!!.setCancelable(false)
                customProgressBar!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun showNoInternetDialog(activity: Activity?) {
            if (noInternetDailog == null) noInternetDailog = NoInternetConnectionDailog.show(
                activity!!,
                true
            )
            try {
                noInternetDailog!!.setCancelable(false)
                noInternetDailog!!.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun dismissNoInternetDialog() {
            try {
                if (null != noInternetDailog && noInternetDailog!!.isShowing()) {
                    noInternetDailog!!.dismiss()
                    noInternetDailog = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun dismissLoadingDialog() {
            try {
                if (null != customProgressBar && customProgressBar!!.isShowing()) {
                    customProgressBar!!.dismiss()
                    customProgressBar = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun getCurrentTimeStamp(): String? {
            val c = Calendar.getInstance()
            return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(c.time)
        }

        fun makeMeBlink(view: View, drawable: Drawable?, duration: Long): Animation? {
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = duration
            anim.startOffset = 1
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            view.startAnimation(anim)
            view.background = drawable
            return anim
        }

        fun printHashKey(pContext: Context) {
            try {
                val info = pContext.packageManager.getPackageInfo(
                    pContext.packageName,
                    PackageManager.GET_SIGNATURES
                )
                for (signature in info.signatures) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    val hashKey = String(Base64.encode(md.digest(), 0))
                    Log.i("TAG", "printHashKey() Hash Key: $hashKey")
                }
            } catch (e: NoSuchAlgorithmException) {
                Log.e("TAG", "printHashKey()", e)
            } catch (e: Exception) {
                Log.e("TAG", "printHashKey()", e)
            }
        }

        fun setRoundImage(
            context: Context?,
            imageView: ImageView?,
            lottie: LottieAnimationView?,
            url: String?
        ) {

            if (lottie != null) {
                lottie.visibility = View.VISIBLE
            }
            Glide.with(context!!).load(checkUrl(url)).listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    if (lottie != null) lottie.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    if (lottie != null) lottie.visibility = View.GONE
                    return false
                }
            }).apply(RequestOptions.bitmapTransform(RoundedCorners(14))).into(imageView!!)
        }

        private fun checkUrl(url: String?): Any {
            if(url.equals(""+ParamEnum.BASE_URL.theValue()+"honey_app/public/img/imagenotfound.jpg")) return "https://i.ibb.co/y6sCcvm/user-thumbnail.jpg"
            else if(url.equals("")) return "https://i.ibb.co/y6sCcvm/user-thumbnail.jpg"
            else if(url==null) return "https://i.ibb.co/y6sCcvm/user-thumbnail.jpg"
            else if(url.equals("http://54.152.130.226/honey_app/public",ignoreCase = true)) return "https://i.ibb.co/y6sCcvm/user-thumbnail.jpg"
            else return url
        }

        fun setNormalImage(
            context: Context?,
            userIv: ImageView?,
            lottie: LottieAnimationView?,
            url: String?
        ) {
            Glide.with(context!!).load(checkUrl(url)).listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any,
                    target: Target<Drawable?>,
                    isFirstResource: Boolean
                ): Boolean {
                    lottie!!.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any,
                    target: Target<Drawable?>,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    lottie!!.visibility = View.GONE
                    return false
                }
            }).into(userIv!!)
        }

        fun getDeviceToken(prefs: SharedPreferenceUtil): String {
            var token:String?=null
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener<String?> { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                token = task.result
                Log.e("token", "" + token)
                prefs.device_token = token
            })

            return token.toString()
        }

        fun showSnackBar(context: Context?, msg: String?) {
            var snackbar: Snackbar? = null
            snackbar = if (context is MainActivity) Snackbar.make((context as Activity).findViewById(
                R.id.viewSnackbar), msg!!, Snackbar.LENGTH_LONG)
            else Snackbar.make((context as Activity).findViewById(android.R.id.content),
                msg!!,
                Snackbar.LENGTH_LONG)
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(ContextCompat.getColor(context,
                android.R.color.holo_red_dark))
            val tv = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
            snackBarView.minimumHeight = 20
            tv.textSize = 14f
            tv.setTextColor(ContextCompat.getColor(context, R.color.white))
            if(msg.equals("Login Token Expire")) loginIntent(context, msg)
            else if(msg.equals("The token field is required.")) loginIntent(context,
                context.getString(
                    R.string.login_first))
            else snackbar.show()
        }

        fun showSnackBarGreen(context: Context?, msg: String?) {
            var snackbar: Snackbar? = null
            snackbar = if (context is MainActivity) Snackbar.make((context as Activity).findViewById(
                R.id.viewSnackbar), msg!!, Snackbar.LENGTH_LONG)
            else Snackbar.make((context as Activity).findViewById(android.R.id.content),
                msg!!,
                Snackbar.LENGTH_LONG)
            val snackBarView = snackbar.view
            snackBarView.setBackgroundColor(ContextCompat.getColor(context,
                android.R.color.holo_green_dark))
            val tv = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
            snackBarView.minimumHeight = 20
            tv.textSize = 14f
            tv.setTextColor(ContextCompat.getColor(context, R.color.white))
            snackbar.show()
        }

        private fun loginIntent(context: Activity, msg: String) {
            val sharedPreferenceUtil= SharedPreferenceUtil.getInstance(context)
            val language=sharedPreferenceUtil.selectedLanguage
            sharedPreferenceUtil.deletePreferences()
            sharedPreferenceUtil.isFirstTime=true
            sharedPreferenceUtil.isLanguageFirstTime=true
            sharedPreferenceUtil.selectedLanguage=language
            getDeviceToken(sharedPreferenceUtil)
            val intent= Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }

//        fun getPickIntent(context: Context, cameraOutputUri: Uri?): Intent? {
//            val intents=ArrayList<Intent>()
//            if (true) intents.add(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
//            if (true) setCameraIntents(context, intents, cameraOutputUri)
//            if (intents.isEmpty()) return null
//            val result = Intent.createChooser(intents.removeAt(0), null)
//            if (!intents.isEmpty()) result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(arrayOf<Parcelable>()))
//            return result
//        }

        fun getPickIntent(context: Context, cameraOutputUri: Uri?): Intent? {
            val intents= ArrayList<Intent>()
            intents.add(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
            setCameraIntents(context,intents, cameraOutputUri)
            if (intents.isEmpty()) return null
            val result = Intent.createChooser(intents.removeAt(0), null)
            if (intents.isNotEmpty()) { result.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents.toArray(arrayOf<Parcelable>())) }
            return result
        }


        private fun setCameraIntents(context:Context,cameraIntents: MutableList<Intent>, output: Uri?) : Boolean{
            var ret=false
            val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val packageManager: PackageManager =context.packageManager
            val listCam = packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val packageName = res.activityInfo.packageName
                val intent = Intent(captureIntent)
                intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
                intent.setPackage(packageName)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, output)
                ret=cameraIntents.add(intent)
            }

            return ret
        }
        fun setVideoIntent(context: Context, cameraIntents: ArrayList<Intent>, output: Uri?){
            val captureIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            //val intent = Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION)

            val packageManager = context.packageManager
            val listCam: List<ResolveInfo> = packageManager.queryIntentActivities(captureIntent, 0)
            for (res in listCam) {
                val packageName: String = res.activityInfo.packageName
                val intent = Intent(captureIntent)
                intent.component = ComponentName(res.activityInfo.packageName,
                    res.activityInfo.name)
                intent.setPackage(packageName)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, output)
                intent.putExtra("uri", output)
                cameraIntents.add(intent)
            }
        }

        fun isGPlayServicesOK(activity: Activity?): Boolean {
            val isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                activity)
            if (isAvailable == ConnectionResult.SUCCESS) return true
            else if (GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)) GoogleApiAvailability.getInstance().getErrorDialog(
                activity,
                isAvailable,
                1001).show()
            else Toast.makeText(activity,
                activity!!.getApplicationContext().getString(R.string.cannot_connect_to_playstore),
                Toast.LENGTH_SHORT).show()
            return false
        }

        fun getGsonInstance(): Gson {
             if (gson == null)
                 gson = Gson()
                return gson!!
        }

        fun getValue(selectedText: String, context: Context): String {
            var value=""
            value=when(selectedText)
            {
                "popular" -> context.getString(R.string.popular)
                "free_delivery" -> context.getString(R.string.free_delivery)
                "nearest" -> context.getString(R.string.nearest)
                "delivery_time" -> context.getString(R.string.delivery_time)
                "price_range" -> context.getString(R.string.price_range)
                else -> ""
            }

            return value
        }

        fun getKey(key: String, context: Context): String {
            var value=""
            value=when(key)
            {
                context.getString(R.string.popular) -> "popular"
                context.getString(R.string.free_delivery) -> "free_delivery"
                context.getString(R.string.nearest) -> "nearest"
                context.getString(R.string.delivery_time) -> "delivery_time"
                context.getString(R.string.price_range) -> "price_range"
                else -> ""
            }
            return value
        }

        fun getTimeAgo(dt: String, context: Context): String? {
            var time = ""
            val server_format1 = "2019-04-04 13:27:36" //server comes format ?
            val myFormat = "yyyy-MM-dd HH:mm:ss" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val date = sdf.parse(dt)
                val your_format = SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date)
                val splitted = your_format.split(" ".toRegex()).toTypedArray()
                val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
                val endDate = dateFormat.parse(your_format)
                val startDate = Calendar.getInstance().time
                var differenceDate = startDate.time - endDate.time
                val completeDate = splitted[0].split("-".toRegex()).toTypedArray()
                val date1 = completeDate[0]
                val month = completeDate[1]
                val year = completeDate[2]
                val days_in_months = GregorianCalendar(year.toInt(), month.toInt(), date1.toInt()).getActualMaximum(
                    Calendar.DAY_OF_MONTH)
                val secounds: Long = 1000 // 1 secound
                val min = 60 * secounds // 1 min
                val hour: Long = 3600000 // 1 hour
                val day: Long = 86400000 // 1 days
                differenceDate = differenceDate - 65000
                val monthdifference = differenceDate / (days_in_months * day)
                val daysDifference = differenceDate / day
                val hourdifference = differenceDate / hour
                val mindifference = differenceDate / min
                val secoundsDiffer = differenceDate / secounds
                time = if (monthdifference > 0) if (monthdifference == 1L) "$monthdifference "+context.getString(
                    R.string.month_ago) else "$monthdifference "+context.getString(R.string.months_ago) else if (daysDifference > 0) if (daysDifference == 1L) "$daysDifference "+context.getString(
                    R.string.day_ago) else "$daysDifference "+context.getString(R.string.days_ago) else if (hourdifference > 0) if (hourdifference == 1L) "$hourdifference "+context.getString(
                    R.string.hour_ago) else "$hourdifference "+context.getString(R.string.hours_ago) else if (mindifference > 0) if (mindifference == 1L) "$mindifference "+context.getString(
                    R.string.min_ago) else "$mindifference "+context.getString(R.string.mins_ago) else if (secoundsDiffer > 0) "$secoundsDiffer "+context.getString(
                    R.string.secs_ago) else context.getString(R.string.now)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time
        }

        fun getTime(dt: String, context: Context): String? {
            var time = ""
            val myFormat = "dd/MM/yyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val date = sdf.parse(dt)
                val your_format = SimpleDateFormat("dd/MM/yyy").format(date)
                val splitted = your_format.split(" ".toRegex()).toTypedArray()
                val dateFormat = SimpleDateFormat("dd/MM/yyy")
                val endDate = dateFormat.parse(your_format)
                val startDate = Calendar.getInstance().time
                var differenceDate = startDate.time - endDate.time
                val completeDate = splitted[0].split("/".toRegex()).toTypedArray()
                val date1 = completeDate[0]
                val month = completeDate[1]
                val year = completeDate[2]
                val days_in_months = GregorianCalendar(year.toInt(), month.toInt(), date1.toInt()).getActualMaximum(
                    Calendar.DAY_OF_MONTH)
                val secounds: Long = 1000 // 1 secound
                val min = 60 * secounds // 1 min
                val hour: Long = 3600000 // 1 hour
                val day: Long = 86400000 // 1 days
                differenceDate = differenceDate - 65000
                val monthdifference = differenceDate / (days_in_months * day)
                val daysDifference = differenceDate / day
                val hourdifference = differenceDate / hour
                val mindifference = differenceDate / min
                val secoundsDiffer = differenceDate / secounds
                time = if (monthdifference > 0) if (monthdifference == 1L) "$monthdifference "+context.getString(
                    R.string.month_ago) else "$monthdifference "+context.getString(R.string.months_ago) else if (daysDifference > 0) if (daysDifference == 1L) "$daysDifference "+context.getString(
                    R.string.day_ago) else "$daysDifference "+context.getString(R.string.days_ago) else if (hourdifference > 0) if (hourdifference == 1L) "$hourdifference "+context.getString(
                    R.string.hour_ago) else "$hourdifference "+context.getString(R.string.hours_ago) else if (mindifference > 0) if (mindifference == 1L) "$mindifference "+context.getString(
                    R.string.min_ago) else "$mindifference "+context.getString(R.string.mins_ago) else if (secoundsDiffer > 0) "$secoundsDiffer "+context.getString(
                    R.string.secs_ago) else context.getString(R.string.now)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time
        }

        fun getDispatchTime(dt: String, context: Context): String? {
            var time = ""
            val myFormat = "dd/MM/yyy" //In which you need put here
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            sdf.timeZone = TimeZone.getTimeZone("GMT")
            try {
                val date = sdf.parse(dt)
                val your_format = SimpleDateFormat("dd/MM/yyy").format(date)
                val splitted = your_format.split(" ".toRegex()).toTypedArray()
                val dateFormat = SimpleDateFormat("dd/MM/yyy")
                val endDate = dateFormat.parse(your_format)
                val startDate = Calendar.getInstance().time
                var differenceDate = endDate.time-startDate.time
                val completeDate = splitted[0].split("/".toRegex()).toTypedArray()
                val date1 = completeDate[0]
                val month = completeDate[1]
                val year = completeDate[2]
                val days_in_months = GregorianCalendar(year.toInt(), month.toInt(), date1.toInt()).getActualMaximum(
                    Calendar.DAY_OF_MONTH)
                val secounds: Long = 1000 // 1 secound
                val min = 60 * secounds // 1 min
                val hour: Long = 3600000 // 1 hour
                val day: Long = 86400000 // 1 days
                differenceDate = differenceDate - 65000
                val monthdifference = differenceDate / (days_in_months * day)
                val daysDifference = differenceDate / day
                val hourdifference = differenceDate / hour
                val mindifference = differenceDate / min
                val secoundsDiffer = differenceDate / secounds
                time = if (monthdifference > 0) if (monthdifference == 1L) "$monthdifference "+context.getString(
                    R.string.month_ago) else "$monthdifference "+context.getString(R.string.months_ago) else if (daysDifference > 0) if (daysDifference == 1L) "$daysDifference "+context.getString(
                    R.string.day_ago) else "$daysDifference "+context.getString(R.string.days_ago) else if (hourdifference > 0) if (hourdifference == 1L) "$hourdifference "+context.getString(
                    R.string.hour_ago) else "$hourdifference "+context.getString(R.string.hours_ago) else if (mindifference > 0) if (mindifference == 1L) "$mindifference "+context.getString(
                    R.string.min_ago) else "$mindifference "+context.getString(R.string.mins_ago) else if (secoundsDiffer > 0) "$secoundsDiffer "+context.getString(
                    R.string.secs_ago) else context.getString(R.string.now)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            return time
        }

        fun getGuestData(key: String): String {
        return TextUtils.join(",", getData(key))
        }

        private fun getData(key: String): List<String> {
           val list=ArrayList<String>()
            if(GuestData!!.instance!!.allData!!.size>0)
            {
                for(i in 0..(GuestData.instance!!.allData!!.size-1))
                {
                    if(key.equals(ParamEnum.PRODUCT_ID.theValue())) list.add(GuestData!!.instance!!.allData!!.get(
                        i).product_id)
                    else if(key.equals(ParamEnum.SELLER_ID.theValue())) list.add(GuestData!!.instance!!.allData!!.get(
                        i).store_id)
                    else if(key.equals(ParamEnum.QUANTITY.theValue())) list.add("" + GuestData!!.instance!!.allData!!.get(
                        i).quanutity)
                }
            }
            return list
        }

        fun getRandomColor(): Int {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

    }
}
