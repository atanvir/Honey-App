package com.frzah.activity.Webview

import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import com.frzah.R
import com.frzah.base.BaseActivity
import com.frzah.utils.CommonUtils.Companion.setToolbar
import com.frzah.utils.ViewExtension.setLocale
import kotlinx.android.synthetic.main.activity_webview.*

class WebviewActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview)
        init()
        initControl()
        myObserver()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        setLocale(this)
        setToolbar(this,""+intent.getStringExtra("title"))

    }

    override fun init() {
        webView.settings.javaScriptEnabled=true
        webView.loadUrl("https://mobuloustech.com/honey_app/api/"+intent.getStringExtra("apiName"))
        webView.webViewClient=object: WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                lvWebView.visibility= View.GONE
            }
        }
    }

    override fun initControl() {
    }

    override fun myObserver() {
    }

}