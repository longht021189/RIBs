package com.uber.rib.core

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.uber.rib.core.Bundle as WrappedBundle

abstract class SimpleRibDialog : DialogFragment() {

    private var routerInstance: IViewRouter<*>? = null

    protected val router: IViewRouter<*>? get() = routerInstance
    protected val interactor: Interactor<*, *>? get() = routerInstance?.interactor

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity
            ?.let { RibDialog(it, theme, this) }
            ?: super.onCreateDialog(savedInstanceState)
    }

    protected open fun onCreateDialog(savedInstanceState: Bundle?, dialog: Dialog) {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val router = createRouter()
        val bundle = if (savedInstanceState != null) {
            WrappedBundle(savedInstanceState)
        } else {
            null
        }

        router.dispatchAttach(bundle)

        routerInstance = router

        return router.view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        routerInstance?.saveInstanceState(WrappedBundle(outState))
    }

    override fun onDestroyView() {
        routerInstance?.dispatchDetach()
        routerInstance = null

        super.onDestroyView()
    }

    protected abstract fun createRouter(): IViewRouter<*>

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