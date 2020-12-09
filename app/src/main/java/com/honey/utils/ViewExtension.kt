package com.honey.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.devs.readmoreoption.ReadMoreOption
import com.honey.R
import java.util.*


object ViewExtension{
    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    fun TAG(any: Any?): String? {
        return any!!.javaClass.simpleName
    }

    fun readMore(context: Context, text: TextView, lines: Int){
        val readMoreOption: ReadMoreOption = ReadMoreOption.Builder(context)
            .textLength(lines, ReadMoreOption.TYPE_LINE) // OR
            //.textLength(300, ReadMoreOption.TYPE_CHARACTER)
            .moreLabel(context.getString(R.string.read_more))
            .lessLabel(context.getString(R.string.undo))
            .moreLabelColor(Color.parseColor("#fea405"))
            .lessLabelColor(Color.BLUE)
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()

        readMoreOption.addReadMoreTo(text,text.text.toString())
    }

    fun setLocale(context: Activity) {
        val prefs = SharedPreferenceUtil.getInstance(context)
        val locale = Locale(prefs.selectedLanguage)
        val config = Configuration(context.resources.configuration)
        Locale.setDefault(locale)
        config.setLocale(locale)
        context.baseContext.resources.updateConfiguration(config, context.baseContext.resources.displayMetrics)
    }


}