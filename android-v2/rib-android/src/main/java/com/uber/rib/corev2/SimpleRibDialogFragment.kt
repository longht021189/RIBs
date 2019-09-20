package com.uber.rib.corev2

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import com.uber.rib.corev2.simple.view.Router
import io.reactivex.android.schedulers.AndroidSchedulers

abstract class SimpleRibDialogFragment : DialogFragment() {

    private var routerInstance: SimpleRouter? = null
    private val listener: SimpleDialog.Listener by lazy {
        object : SimpleDialog.Listener {
            override fun onCreateDialog(dialog: SimpleDialog) {
                this@SimpleRibDialogFragment.onCreateDialog(dialog)
            }

            override fun onBackPressed(): Boolean {
                return this@SimpleRibDialogFragment.onBackPressed()
            }
        }
    }

    protected val router: SimpleRouter? get() = routerInstance

    override fun onCreateDialog(savedInstanceState: Bundle?): SimpleDialog {
        return SimpleDialog(
            listener = listener,
            context = context,
            theme = theme
        )
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        createRouter().apply {
            dispatchAttach(savedInstanceState)

            if (this is Router) {
                val provider = AndroidLifecycleScopeProvider
                    .from(this@SimpleRibDialogFragment)

                this.view.take(1)
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDisposable(provider)
                    .subscribe { getContentView(view).addView(it) }
            }

            routerInstance = this
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

    override fun onDestroyView() {
        routerInstance?.dispatchDetach()
        routerInstance = null

        super.onDestroyView()
    }

    protected open fun onCreateDialog(dialog: SimpleDialog) { }

    protected open fun onBackPressed(): Boolean {
        return (routerInstance?.handleBackPress() == true)
    }

    protected open fun getContentView(view: View): ViewGroup {
        return view as ViewGroup
    }

    protected abstract fun createRouter(): SimpleRouter
}