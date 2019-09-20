package com.uber.rib.core

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.uber.rib.core.Bundle as WrappedBundle

abstract class SimpleRibDialog : DialogFragment() {

    private var routerInstance: Router<*>? = null

    protected val router: Router<*>? get() = routerInstance
    protected val interactor: Interactor<*, *>? get() = routerInstance?.interactor

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity
            ?.let { RibDialog(it, theme, this) }
            ?: super.onCreateDialog(savedInstanceState)
    }

    protected open fun onCreateDialog(savedInstanceState: Bundle?, dialog: Dialog) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val router = createRouter()
        val bundle = if (savedInstanceState != null) {
            WrappedBundle(savedInstanceState)
        } else {
            null
        }

        router.dispatchAttach(bundle)

        if (router is IViewRouter<*>) {
            getContentView(view).addView(router.view)
        }

        routerInstance = router
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        routerInstance?.saveInstanceState(WrappedBundle(outState))
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

    protected open fun getContentView(view: View): ViewGroup {
        return view as ViewGroup
    }

    protected abstract fun createRouter(): Router<*>

    class RibDialog(
        context: Context, themeResId: Int,
        private val fragment: SimpleRibDialog
    ) : Dialog(context, themeResId) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            fragment.onCreateDialog(savedInstanceState, this)
        }
    }
}