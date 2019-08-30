package com.uber.rib.android.lazy

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.google.common.base.Optional
import io.reactivex.Observable
import io.reactivex.Observer

interface IPresenterLazy<T: View> {
    val view: Observable<T>
    val attachedView: Observable<Optional<T>>

    fun onViewAttachedToWindow(view: T)
    fun onViewDetachedFromWindow(view: T)

    class Builder {

        private val type: BuilderType
        private val resId: Int
        private var observer: Observer<*>? = null

        private lateinit var viewGroup: ViewGroup
        private lateinit var context: Context
        private lateinit var presenter: IPresenterLazy<*>

        constructor(parent: ViewGroup, @LayoutRes resId: Int) {
            this.type = BuilderType.ViewGroup
            this.resId = resId
            this.viewGroup = parent
        }
        constructor(context: Context, @LayoutRes resId: Int) {
            this.type = BuilderType.Context
            this.resId = resId
            this.context = context
        }
        constructor(parent: IPresenterLazy<*>, @LayoutRes resId: Int) {
            this.type = BuilderType.IPresenterLazy
            this.resId = resId
            this.presenter = parent
        }

        fun build(listener: AsyncLayoutInflater.OnInflateFinishedListener) {
            when (type) {
                BuilderType.Context -> {
                    AsyncLayoutInflater(context).inflate(resId, null, listener)
                }
                BuilderType.ViewGroup -> {
                    AsyncLayoutInflater(viewGroup.context).inflate(resId, viewGroup, listener)
                }
                BuilderType.IPresenterLazy -> {
                    val observer = object : WeakObserver.SimpleObserver<View>() {
                        override fun onNext(t: View) {
                            AsyncLayoutInflater(t.context).inflate(resId, t as? ViewGroup, listener)
                        }
                    }
                    presenter.view.take(1).subscribe(observer)
                    this.observer = observer
                }
            }
        }
    }

    private enum class BuilderType {
        ViewGroup,
        Context,
        IPresenterLazy
    }
}