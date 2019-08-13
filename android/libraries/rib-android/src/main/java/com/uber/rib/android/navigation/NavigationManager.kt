package com.uber.rib.android.navigation

import androidx.collection.ArrayMap
import java.lang.ref.WeakReference

open class NavigationManager {

    private val map by lazy {
        ArrayMap<String, WeakReference<NavigationData>>()
    }

    fun addNavigation(id: String, data: NavigationData) {
        map[id] = WeakReference(data)
    }

    fun getNavigation(id: String): Navigation? {
        return map[id]?.get()?.navigation
    }
}