package com.frzah.activity.Splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.annotation.Nullable
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.frzah.R
import com.frzah.activity.Main.MainActivity
import com.frzah.activity.SelectLanguage.SelectLanguageActivity
import com.frzah.base.BaseActivity
import com.frzah.firebase.MyFirebaseMessageService
import com.frzah.utils.CommonUtils.Companion.DELAY_FOR_NEXT_SCREEN
import com.frzah.utils.CommonUtils.Companion.DELAY_GIF_BACKGROUND
import com.frzah.utils.CommonUtils.Companion.getDeviceToken
import com.frzah.utils.FixAppBarLayoutBehavior
import com.frzah.utils.GetContext
import com.frzah.utils.ViewExtension.setLocale
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_shop_details.*
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity: BaseActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        init()
        initControl()
        myObserver()
    }

    override fun onResume() {
        super.onResume()
        val getContext: GetContext by lazy { GetContext.getInstance(applicationContext) }
        getDeviceToken(prefs)
        setLocale(this)
    }


    override fun init() {
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        startGif()
    }

    override fun initControl() {
    }

    override fun myObserver() {
    }

    private fun startGif() {
        try {
            Glide.with(this).asGif().load(R.drawable.splsh).listener(object :
                RequestListener<GifDrawable?> {
                override fun onLoadFailed(@Nullable e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<GifDrawable?>?, isFirstResource: Boolean): Boolean {
                    startIntent()
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable?>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    Handler().postDelayed({
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) clMain.backgroundTintList = getColorStateList(R.color.app_theme_color)
                    }, DELAY_GIF_BACKGROUND)
                    startIntent()
                    return false
                }
            }).into(image)
        } catch (ex: Exception) {
            startIntent()
        }
    }

    private fun startIntent() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (prefs.isLanguageFirstTime!! && prefs.isFirstTime!!) {
                val intent = Intent(this, MainActivity::class.java)
                if (getIntent().getStringExtra("title") != null) {
                    intent.putExtra("cameFrom", MyFirebaseMessageService::class.simpleName)
                    intent.putExtra("body", getIntent().getStringExtra("body"))
                }
                startActivity(intent)
                finish()
            }/* else if (prefs.isLanguageFirstTime!! && !(prefs.isFirstTime!!)) {
                startActivity(Intent(this, WalkThroughActivity::class.java))
                finish()
            }*/ else {
                startActivity(Intent(this, SelectLanguageActivity::class.java))
                finish()
            }
        }, DELAY_FOR_NEXT_SCREEN)
    }


}