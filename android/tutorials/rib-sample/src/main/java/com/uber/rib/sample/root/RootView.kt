package com.uber.rib.sample.root

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RootView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), RootInteractor.RootPresenter {

    private val clickSubject by lazy {
        PublishSubject.create<Any>()
    }

    override val click: Observable<Any>
        get() = clickSubject

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        setOnClickListener {
            clickSubject.onNext(Any())
        }
    }
}
