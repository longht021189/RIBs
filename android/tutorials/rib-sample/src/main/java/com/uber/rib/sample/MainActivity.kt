package com.uber.rib.sample

import android.view.ViewGroup
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.RibActivity
import com.uber.rib.core.ViewRouter
import com.uber.rib.sample.root.RootBuilder
import dagger.android.DispatchingAndroidInjector
import javax.inject.Inject

class MainActivity : RibActivity() {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*> {
        DaggerMainActivityComponent
            .factory().create(this).inject(this)

        return RootBuilder().build(injector)
    }
}
