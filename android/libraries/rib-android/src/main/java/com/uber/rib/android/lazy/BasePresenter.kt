package com.uber.rib.android.lazy

import android.view.View
import androidx.annotation.CallSuper
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.google.common.base.Optional
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

abstract class BasePresenter<T: View>(
    builder: IPresenterLazy.Builder
) : IPresenterLazy<T> {

    protected open val attachedViewSubject by lazy {
        BehaviorSubject.create<Optional<T>>()
    }

    protected open val viewSubject by lazy {
        BehaviorSubject.create<T>()
    }
    private val listener by lazy {
        AsyncLayoutInflater.OnInflateFinishedListener {
                view, _, _ -> onInflateFinished(view)
        }
    }

    override val view: Observable<T> get() {
        return viewSubject
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    override val attachedView: Observable<Optional<T>> get() {
        return attachedViewSubject
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    init {
        builder.build(listener)
    }

    @CallSuper
    override fun onViewAttachedToWindow(view: T) {
        attachedViewSubject.onNext(Optional.of(view))
    }

    @CallSuper
    override fun onViewDetachedFromWindow(view: T) {
        attachedViewSubject.onNext(Optional.absent())
    }

    protected open fun onInflateFinished(view: View) {
        view.addOnAttachStateChangeListener(OnAttachStateChangeListener(this))

        @Suppress("UNCHECKED_CAST")
        viewSubject.onNext(view as T)
    }
}