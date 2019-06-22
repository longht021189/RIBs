package com.uber.rib.sample.dialog

import android.app.Dialog
import android.content.Context
import android.view.KeyEvent
import android.view.ViewGroup
import android.view.WindowManager
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.RibDialogFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MyDialog : RibDialogFragment() {

    @Inject
    lateinit var injector: DispatchingAndroidInjector<Any>

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateDialog(dialog: Dialog) {
        val window = dialog.window
        if (window != null) {
            window.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT)

            window.setBackgroundDrawableResource(android.R.color.transparent)
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            window.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
        }

        dialog.setOnKeyListener { _, _, event ->
            if (event.action == KeyEvent.ACTION_UP &&
                event.keyCode == KeyEvent.KEYCODE_BACK) {
                onBackPressed()
            }

            (event.keyCode == KeyEvent.KEYCODE_BACK)
        }
    }

    override fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*> {
        return MyDialogBuilder().build(injector)
    }

    /*private fun processIsEnabledFlag(value: Boolean) {
        val w = dialog?.window

        when {
            (w != null && value) -> {
                w.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                w.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            }
            (w != null) -> {
                w.addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                w.addFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
            }
        }
    }*/

    private fun onBackPressed() {
        dismiss()
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    annotation class MyDialogScope
}