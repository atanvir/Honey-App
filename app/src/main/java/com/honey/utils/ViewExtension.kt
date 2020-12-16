package com.honey.utils

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.devs.readmoreoption.ReadMoreOption
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.honey.R
import kotlinx.android.synthetic.main.activity_main.*
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
        when(text.id){
            R.id.tvDesc -> {
                characterWiseReadMore(context, text, lines)
            }
            else ->{ lineWiseReadMore(context, text, lines) }
        }
    }

    fun characterWiseReadMore(context: Context, text: TextView, lines: Int){
        val readMoreOption : ReadMoreOption = ReadMoreOption.Builder(context)
            .textLength(40, ReadMoreOption.TYPE_CHARACTER)
            .moreLabel(context.getString(R.string.read_more))
            .lessLabel(context.getString(R.string.undo))
            .moreLabelColor(Color.parseColor("#fea405"))
            .lessLabelColor(Color.BLUE)
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()
        readMoreOption.addReadMoreTo(text, text.text.toString())
    }

    fun lineWiseReadMore(context: Context, text: TextView, lines: Int){
        val readMoreOption : ReadMoreOption = ReadMoreOption.Builder(context)
            .textLength(lines, ReadMoreOption.TYPE_LINE)
            .moreLabel(context.getString(R.string.read_more))
            .lessLabel(context.getString(R.string.undo))
            .moreLabelColor(Color.parseColor("#fea405"))
            .lessLabelColor(Color.BLUE)
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()
        readMoreOption.addReadMoreTo(text, text.text.toString())
    }
    fun findBadgeId(context: Context, bottomNavigationView: BottomNavigationMenuView) : TextView {
        val v: View = bottomNavigationView.getChildAt(3)
        val itemView = v as BottomNavigationItemView
        val badge: View = LayoutInflater.from(context).inflate(R.layout.menu_unread_message_layout,
            bottomNavigationView,
            false)
        itemView.addView(badge)
        for (i in 0 until bottomNavigationView.getChildCount()) {
            val iconView: View = bottomNavigationView.getChildAt(i).findViewById(R.id.icon)
            val layoutParams = iconView.layoutParams
            val displayMetrics = context.resources.displayMetrics
            if (i == 1) {
                layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24f,
                    displayMetrics).toInt()
                layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    44f,
                    displayMetrics).toInt()
            } else {
                layoutParams.height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    24f,
                    displayMetrics).toInt()
                layoutParams.width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    44f,
                    displayMetrics).toInt()
            }
            iconView.layoutParams = layoutParams
        }

        return badge.findViewById(R.id.notification_badge)
    }

    fun setLocale(context: Context?) : ContextWrapper {
        val prefs=SharedPreferenceUtil.getInstance(context!!)
        val locale=Locale(prefs.selectedLanguage)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources.updateConfiguration(config, context.resources.getDisplayMetrics())
        return ContextWrapper(context)
    }


}