package com.uber.rib.sample

import android.view.ViewGroup
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.RibActivity
import com.uber.rib.core.Router
import javax.inject.Inject
import javax.inject.Provider

class MainActivity : RibActivity() {

    @Inject
    lateinit var navigation: Navigation

    @Inject
    lateinit var map: Map<String, @JvmSuppressWildcards Provider<Router<*>>>

    override fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*> {
        DaggerMainActivityComponent
            .factory().create(this).inject(this)

        return map["ROOT"]?.get() as IViewRouter<*>
    }

    override fun onBackPressed() {
        if (navigation.onBackPressed()) {
            return
        }

        super.onBackPressed()
    }
}
