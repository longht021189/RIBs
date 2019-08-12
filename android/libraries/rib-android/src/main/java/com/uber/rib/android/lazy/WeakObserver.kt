package com.uber.rib.android.lazy

import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

class WeakObserver<T>(
    observer: Observer<T>
) : Observer<T> {

    private val observerRef = WeakReference(observer)
    private var disposableRef: Disposable? = null

    val disposable: Disposable? get() = disposableRef

    override fun onComplete() {
        observerRef.get()?.onComplete()
    }

    override fun onSubscribe(d: Disposable) {
        disposableRef = d
    }

    override fun onNext(t: T) {
        val observer = observerRef.get()
        if (observer == null) {
            if (disposableRef?.isDisposed == false) {
                disposableRef?.dispose()
            }
        } else {
            observer.onNext(t)
        }
    }

    override fun onError(e: Throwable) {
        observerRef.get()?.onError(e)
    }

    open class SimpleObserver<T> : Observer<T> {
        override fun onComplete() {}
        override fun onSubscribe(d: Disposable) {}
        override fun onNext(t: T) {}
        override fun onError(e: Throwable) {}
    }
}