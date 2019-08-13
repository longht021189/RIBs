package com.uber.rib.android.lazy

import android.view.View
import java.lang.ref.WeakReference

class OnAttachStateChangeListener<T: View>(
    presenter: IPresenterLazy<T>
) : View.OnAttachStateChangeListener {
    private val presenterRef = WeakReference(presenter)

    override fun onViewDetachedFromWindow(view: View) {}

    override fun onViewAttachedToWindow(view: View) {
        @Suppress("UNCHECKED_CAST")
        presenterRef.get()?.onViewAttachedToWindow(view as T)
    }
}