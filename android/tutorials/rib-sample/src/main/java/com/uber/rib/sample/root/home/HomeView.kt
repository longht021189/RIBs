package com.uber.rib.sample.root.home

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import com.uber.rib.sample.NavigationUtil

class HomeView @JvmOverloads constructor(
    context: Context,
    private val navigation: NavigationUtil,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), HomeInteractor.HomePresenter {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setBackgroundColor(Color.BLUE)

        setOnClickListener {
            navigation
                .getNodeManager(NavigationUtil.BACK_STACK_MAIN)
                .add(NavigationUtil.SCREEN_SPLASH, null)
        }
    }

    override fun onDetachedFromWindow() {
        setOnClickListener(null)

        super.onDetachedFromWindow()
    }
}
