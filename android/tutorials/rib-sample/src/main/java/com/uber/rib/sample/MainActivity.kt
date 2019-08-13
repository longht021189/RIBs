package com.uber.rib.sample

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.RibActivity
import com.uber.rib.core.RibRefWatcher
import com.uber.rib.core.Router
import com.uber.rib.core.navigation.Navigation
import com.uber.rib.android.navigation.Navigation as Nav
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : RibActivity() {

    @Inject lateinit var map: Map<String, @JvmSuppressWildcards Provider<Router<*>>>
    @Inject lateinit var navigation: NavigationManager

    private var rootNavigation: Nav? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        RibRefWatcher.getInstance().apply {
            enableBreadcrumbLogging()
            setReferenceWatcher(ReferenceWatcher())
        }
        super.onCreate(savedInstanceState)
    }

    override fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*> {
        val router = map["ROOT"]?.get()!!
        rootNavigation = navigation.createIfNotExists(router.tag)
        return router as IViewRouter<*>
    }

    override fun getNavigation(): Navigation? {
        return null
    }

    private class ReferenceWatcher : RibRefWatcher.ReferenceWatcher {
        override fun watch(instance: Any?) {}
        override fun logBreadcrumb(eventType: String?, data: String?, parent: String?) {
            Log.i("DEBUG", "eventType = $eventType, data = $data, parent = $parent")
        }
    }
}
