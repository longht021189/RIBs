package com.uber.rib.corev2

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import java.lang.ref.WeakReference

open class SimpleDialog @JvmOverloads constructor(
    listener: Listener,
    context: Context?,
    theme: Int = 0
) : AppCompatDialog(context, theme) {

    private val listenerRef = WeakReference(listener)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        listenerRef.get()?.onCreateDialog(dialog = this)
    }

    override fun onBackPressed() {
        if (listenerRef.get()?.onBackPressed() != true) {
            super.onBackPressed()
        }
    }

    interface Listener {
        fun onCreateDialog(dialog: SimpleDialog)
        fun onBackPressed(): Boolean
    }
}