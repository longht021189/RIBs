package com.uber.rib.core.simple.view

import android.os.Bundle
import android.view.View
import com.uber.rib.core.simple.Router as Base
import com.uber.rib.core.simple.InteractorHelper
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class Router(
    interactor: Interactor,
    private val builder: ViewBuilder
) : Base(interactor) {

    private val viewSubject by lazy {
        BehaviorSubject.create<View>()
    }
    private val attachStateSubject by lazy {
        BehaviorSubject.createDefault(false)
    }

    val view: Observable<View> get() {
        return viewSubject
    }
    val attachedView: Observable<Optional>

    override val interactor: Interactor
        get() = super.interactor as Interactor

    override fun dispatchAttach(savedInstanceState: Bundle?) {
        super.dispatchAttach(savedInstanceState)

        if (viewSubject.value == null) {
            builder.build(BuildViewCallback(
                viewSubject, interactor, attachStateSubject))
        }
    }

    private class BuildViewCallback(
        private val viewSubject: Subject<View>,
        private val interactor: Interactor,
        private val attachStateSubject: Subject<Boolean>
    ) : ViewBuilder.Callback, View.OnAttachStateChangeListener {

        override fun onInflateFinished(view: View) {
            viewSubject.onNext(view)
            interactor.onInflateFinished(view)

            view.addOnAttachStateChangeListener(this)
        }

        override fun onViewAttachedToWindow(view: View) {
            interactor.onViewAttachedToWindow(view)
            attachStateSubject.onNext(true)
        }

        override fun onViewDetachedFromWindow(view: View) {
            attachStateSubject.onNext(false)
            interactor.onViewDetachedFromWindow(view)
        }
    }
}