package com.honey.utils

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.devs.readmoreoption.ReadMoreOption


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
            .moreLabel("Read More")
            .lessLabel("Undo")
            .moreLabelColor(Color.parseColor("#fea405"))
            .lessLabelColor(Color.BLUE)
            .labelUnderLine(true)
            .expandAnimation(true)
            .build()

        readMoreOption.addReadMoreTo(text,text.text.toString())
    }


}