package com.frzah.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.frzah.utils.CommonUtils.Companion.PERSISTABLE_PREFRENCE_NAME
import com.frzah.utils.CommonUtils.Companion.PREFRENCE_NAME

class SharedPreferenceUtil
private constructor(val context: Context) {
    val TAG = SharedPreferenceUtil::class.java.simpleName
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFRENCE_NAME, Context.MODE_PRIVATE)
    private val persistableSharedPreferences: SharedPreferences = context.getSharedPreferences(PERSISTABLE_PREFRENCE_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val persistableEditor: SharedPreferences.Editor = persistableSharedPreferences.edit()


    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: SharedPreferenceUtil? = null

        fun getInstance(ctx: Context): SharedPreferenceUtil {
            if (instance == null) {
                instance = SharedPreferenceUtil(ctx)
            }
            return instance!!
        }

    }

    var isFirstTime: Boolean?
        get() = sharedPreferences["isFirstTime", false]!!
        set(value) = sharedPreferences.set("isFirstTime", value)

    var isLanguageFirstTime: Boolean?
        get() = sharedPreferences["isLanguageFirstTime", false]!!
        set(value) = sharedPreferences.set("isLanguageFirstTime", value)

    var isLogin: Boolean?
        get() = sharedPreferences["isLogin", false]!!
        set(value) = sharedPreferences.set("isLogin", value)

    var selectedLanguage: String
        get() = sharedPreferences["selectedLanguage", "en"]!!
        set(value) = sharedPreferences.set("selectedLanguage", value)

    var cameFrom: String
        get() = sharedPreferences["cameFrom", ""]!!
        set(value) = sharedPreferences.set("cameFrom", value)


    var subTotal: String
        get() = sharedPreferences["subTotal", ""]!!
        set(value) = sharedPreferences.set("subTotal", value)

    var tax: String
        get() = sharedPreferences["tax", ""]!!
        set(value) = sharedPreferences.set("tax", value)

    var total: String
        get() = sharedPreferences["total", ""]!!
        set(value) = sharedPreferences.set("total", value)

    var item: String
        get() = sharedPreferences["item", ""]!!
        set(value) = sharedPreferences.set("item", value)

    var discount: String
        get() = sharedPreferences["discount", ""]!!
        set(value) = sharedPreferences.set("discount", value)

    var shippingCharges: String
        get() = sharedPreferences["shippingCharges", ""]!!
        set(value) = sharedPreferences.set("shippingCharges", value)


    var latitude: String
        get() = sharedPreferences["latitude", ""]!!
        set(value) = sharedPreferences.set("latitude", value)

    var longitude: String
        get() = sharedPreferences["longitude", ""]!!
        set(value) = sharedPreferences.set("longitude", value)


    var soldBy: String
        get() = sharedPreferences["soldBy", ""]!!
        set(value) = sharedPreferences.set("soldBy", value)


    var device_token: String?
        get() = sharedPreferences["device_token", ""]!!
        set(value) = sharedPreferences.set("device_token", value)

    var jwtToken: String?
        get() = sharedPreferences["jwtToken", ""]!!
        set(value) = sharedPreferences.set("jwtToken", value)

    var phone_code: String?
        get() = sharedPreferences["phone_code", ""]!!
        set(value) = sharedPreferences.set("phone_code", value)

    var phone: String?
        get() = sharedPreferences["phone", ""]!!
        set(value) = sharedPreferences.set("phone", value)

    var id: String?
        get() = sharedPreferences["id", ""]!!
        set(value) = sharedPreferences.set("id", value)

    var name: String?
        get() = sharedPreferences["name", "Guest"]!!
        set(value) = sharedPreferences.set("name", value)

    var email: String?
        get() = sharedPreferences["email", ""]!!
        set(value) = sharedPreferences.set("email", value)

    var coupon_code: String?
        get() = sharedPreferences["coupon_code", ""]!!
        set(value) = sharedPreferences.set("coupon_code", value)

    var image: String?
        get() = sharedPreferences["image", "https://i.ibb.co/y6sCcvm/user-thumbnail.jpg"]!!
        set(value) = sharedPreferences.set("image", value)


    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit({ it.putString(key, value) })
            is Int -> edit({ it.putInt(key, value) })
            is Boolean -> edit({ it.putBoolean(key, value) })
            is Float -> edit({ it.putFloat(key, value) })
            is Long -> edit({ it.putLong(key, value) })
            else -> Log.e("TAG", "Setting shared pref failed for key: $key and value: $value ")
        }
    }

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }



    inline operator fun <reified T : Any> SharedPreferences.get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun deletePreferences() {
        editor.clear()
        editor.apply()
    }

    fun deletePreference() {
        persistableEditor.clear()
        persistableEditor.apply()
    }
}