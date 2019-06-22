package com.uber.rib.sample

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton
import com.uber.rib.core.navigation.Navigation as Base

@Singleton
class Navigation @Inject constructor(): Base() {

    init {
        createBackStack(BACK_STACK_MAIN, SCREEN_SPLASH)
    }

    companion object {
        const val BACK_STACK_MAIN = "MAIN"

        val SCREEN_SPLASH: Uri = Uri.parse("/ROOT/SPLASH")
        val SCREEN_HOME: Uri   = Uri.parse("/ROOT/HOME")

        const val NODE_ROOT   = "ROOT"
        const val NODE_SPLASH = "SPLASH"
        const val NODE_HOME   = "HOME"
    }
}