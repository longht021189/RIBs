package com.uber.rib.sample.root.splash

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View

class SplashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), SplashInteractor.SplashPresenter {
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setBackgroundColor(Color.CYAN)
    }
}
