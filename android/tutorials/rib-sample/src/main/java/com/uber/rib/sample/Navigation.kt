package com.uber.rib.sample

import android.net.Uri
import javax.inject.Inject
import javax.inject.Singleton
import com.uber.rib.core.navigation.Navigation as Base

@Singleton
class Navigation @Inject constructor(): Base() {
    init {
        createBackStack("MAIN", Uri.parse("/ROOT/SPLASH"))
    }
}