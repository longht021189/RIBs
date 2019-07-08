package com.uber.rib.sample

import android.net.Uri
import com.uber.rib.sample.root.RootInteractor
import com.uber.rib.sample.root.home.HomeInteractor
import com.uber.rib.sample.root.splash.SplashInteractor
import javax.inject.Inject
import javax.inject.Singleton
import com.uber.rib.core.navigation.Navigation as Base

@Singleton
class NavigationUtil @Inject constructor(): Base() {

    init {
        createBackStack(BACK_STACK_MAIN, SCREEN_HOME, null)
    }

    companion object {
        const val BACK_STACK_MAIN = "MAIN"

        val SCREEN_SPLASH: Uri = Uri.parse("/${RootInteractor.NAME}/${SplashInteractor.NAME}")
        val SCREEN_HOME: Uri   = Uri.parse("/${RootInteractor.NAME}/${HomeInteractor.NAME}")
    }
}