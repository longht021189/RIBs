package com.uber.rib.core

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.uber.rib.core.Bundle as WrappedBundle

abstract class SimpleRibActivity : AppCompatActivity() {

    private var routerInstance: Router<*>? = null

    protected val router: Router<*>? get() = routerInstance
    protected val interactor: Interactor<*, *>? get() = routerInstance?.interactor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onPreCreate(savedInstanceState)

        val router = createRouter()
        val bundle = if (savedInstanceState != null) {
            WrappedBundle(savedInstanceState)
        } else {
            null
        }

        router.dispatchAttach(bundle)

        if (router is IViewRouter<*>) {
            getContentView().addView(router.view)
        }

        routerInstance = router
    }

    override fun onBackPressed() {
        if (routerInstance?.handleBackPress() == true) return

        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        routerInstance?.saveInstanceState(WrappedBundle(outState))
    }

    override fun onDestroy() {
        routerInstance?.dispatchDetach()
        routerInstance = null

        super.onDestroy()
    }

    protected open fun onPreCreate(savedInstanceState: Bundle?) {}

    protected open fun getContentView(): ViewGroup {
        return findViewById(android.R.id.content)
    }

    protected abstract fun createRouter(): Router<*>
}