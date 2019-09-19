package com.uber.rib.core

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import com.uber.rib.core.simple.view.Router
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class SimpleRibActivity : AppCompatActivity() {

    private var routerInstance: SimpleRouter? = null

    protected val router: SimpleRouter? get() = routerInstance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createRouter().apply {
            dispatchAttach(savedInstanceState)

            if (this is Router) {
                val provider = AndroidLifecycleScopeProvider
                    .from(this@SimpleRibActivity)

                view.take(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDisposable(provider)
                    .subscribe { getContentView().addView(it) }
            }

            routerInstance = this
        }
    }

    override fun onBackPressed() {
        if (routerInstance?.handleBackPress() != true) {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        routerInstance?.saveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (router?.onActivityResult(requestCode, resultCode, data) != true) {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (router?.onRequestPermissionsResult(requestCode, permissions, grantResults) != true) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroy() {
        routerInstance?.dispatchDetach()
        routerInstance = null

        super.onDestroy()
    }

    protected open fun getContentView(): ViewGroup {
        return findViewById(android.R.id.content)
    }

    protected abstract fun createRouter(): SimpleRouter
}