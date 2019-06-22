package com.uber.rib.sample

import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.RibActivity
import com.uber.rib.sample.dialog.MyDialog
import com.uber.rib.sample.root.RootBuilder
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : RibActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyDialog().show(supportFragmentManager, "DIALOG")
    }

    override fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*> {
        DaggerMainActivityComponent
            .factory().create(this).inject(this)

        return RootBuilder().build(injector)
    }

    override fun onBackPressed() {
        if (navigation.onBackPressed()) {
            return
        }

        super.onBackPressed()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }
}
