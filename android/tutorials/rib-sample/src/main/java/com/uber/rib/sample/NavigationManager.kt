package com.uber.rib.sample

import com.uber.rib.android.navigation.Navigation
import com.uber.rib.android.navigation.NavigationData
import javax.inject.Inject
import javax.inject.Singleton
import com.uber.rib.android.navigation.NavigationManager as Base

@Singleton
class NavigationManager @Inject constructor(): Base() {

    fun createIfNotExists(id: String): Navigation {
        val nav = getNavigation(id)

        return if (nav == null) {
            val data = NavigationData(
                name = id,
                tag = id,
                manager = this
            )

            addNavigation(id, data)
            data.navigation
        } else {
            nav
        }
    }
}