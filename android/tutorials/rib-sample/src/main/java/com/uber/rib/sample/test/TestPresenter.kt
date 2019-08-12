package com.uber.rib.sample.test

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import javax.inject.Inject
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.uber.rib.android.lazy.IPresenterLazy
import com.uber.rib.android.lazy.WeakObserver
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers

class TestPresenter : TestInteractor.TestPresenter, IPresenterLazy<TestView> {

    private val viewSubject by lazy {
        BehaviorSubject.create<TestView>()
    }
    private val listener by lazy {
        AsyncLayoutInflater.OnInflateFinishedListener {
                view, _, _ -> onInflateFinished(view)
        }
    }
    private var observer: Observer<*>? = null

    override val view: Observable<TestView> get() {
        return viewSubject
            .observeOn(AndroidSchedulers.mainThread())
    }

    constructor(parent: ViewGroup, @LayoutRes resId: Int) {
        AsyncLayoutInflater(parent.context).inflate(resId, parent, listener)
    }
    constructor(context: Context, @LayoutRes resId: Int) {
        AsyncLayoutInflater(context).inflate(resId, null, listener)
    }
    constructor(parent: IPresenterLazy<View>, @LayoutRes resId: Int) {
        val observer = object : WeakObserver.SimpleObserver<View>() {
            override fun onNext(t: View) {
                AsyncLayoutInflater(t.context).inflate(resId, t as? ViewGroup, listener)
            }
        }
        parent.view.take(1).subscribe(observer)
        this.observer = observer
    }

    private fun onInflateFinished(view: View) {
        @Suppress("UNCHECKED_CAST")
        viewSubject.onNext(view as TestView)
    }
}
