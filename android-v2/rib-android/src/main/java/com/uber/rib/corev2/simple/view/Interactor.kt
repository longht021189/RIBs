package com.uber.rib.corev2.simple.view

import android.view.View
import com.uber.rib.corev2.simple.Interactor as Base

abstract class Interactor : Base() {

    open fun onInflateFinished(view: View) { }

    open fun onViewAttachedToWindow(view: View) { }

    open fun onViewDetachedFromWindow(view: View) { }
}