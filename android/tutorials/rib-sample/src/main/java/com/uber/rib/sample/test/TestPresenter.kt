package com.uber.rib.sample.test

import android.view.View
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.uber.rib.android.lazy.IPresenterLazy
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@TestBuilder.TestScope
class TestPresenter @Inject constructor(
    @TestBuilder.TestQualifier
    builder: IPresenterLazy.Builder
) : TestInteractor.TestPresenter, IPresenterLazy<TestView> {

    private val viewSubject by lazy {
        BehaviorSubject.create<TestView>()
    }
    private val listener by lazy {
        AsyncLayoutInflater.OnInflateFinishedListener {
                view, _, _ -> onInflateFinished(view)
        }
    }

    override val view: Observable<TestView> get() {
        return viewSubject
            .observeOn(AndroidSchedulers.mainThread())
    }

    init {
        builder.build(listener)
    }

    private fun onInflateFinished(view: View) {
        @Suppress("UNCHECKED_CAST")
        viewSubject.onNext(view as TestView)
    }
}
