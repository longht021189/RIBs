package com.uber.rib.sample

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.RibActivity
import com.uber.rib.core.RibRefWatcher
import com.uber.rib.core.navigation.Navigation
import dagger.android.AndroidInjection

class MainActivity : RibActivity() {

    //@Inject lateinit var map: Map<String, @JvmSuppressWildcards Provider<Router<*>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        RibRefWatcher.getInstance().apply {
            enableBreadcrumbLogging()
            setReferenceWatcher(ReferenceWatcher())
        }
        super.onCreate(savedInstanceState)
    }

    override fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*> {
        /*val router = map["ROOT"]?.get()!!
        return router as IViewRouter<*>*/
        TODO()
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
