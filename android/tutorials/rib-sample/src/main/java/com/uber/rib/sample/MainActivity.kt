package com.uber.rib.sample

import android.os.Bundle
import android.view.ViewGroup
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.RibActivity
import com.uber.rib.core.Router
import dagger.android.AndroidInjection
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : RibActivity() {

    @Inject
    lateinit var navigation: NavigationUtil

    @Inject
    lateinit var map: Map<String, @JvmSuppressWildcards Provider<Router<*>>>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*> {
        return map["ROOT"]?.get() as IViewRouter<*>
    }

    override fun onBackPressed() {
        if (navigation.onBackPressed()) {
            return
        }

        super.onBackPressed()
    }
}
