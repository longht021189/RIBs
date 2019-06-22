package com.uber.rib.sample.splash

import android.content.Context
import android.util.AttributeSet
import android.view.View
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SplashView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), SplashInteractor.SplashPresenter {
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
