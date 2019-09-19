package com.uber.rib.core.simple.view

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.asynclayoutinflater.view.AsyncLayoutInflater
import com.uber.autodispose.ScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

class ViewBuilder @JvmOverloads constructor(
    @LayoutRes private val layoutId: Int,

    context: Context? = null,
    parentView: ViewGroup? = null,
    parentRouter: Router? = null
) {
    private val contextRef = WeakReference(context)
    private val parentViewRef = WeakReference(parentView)
    private val parentRouterRef = WeakReference(parentRouter)

    @Throws(IllegalArgumentException::class)
    fun build(callback: Callback) {
        val context = contextRef.get()
        val parentView = parentViewRef.get()
        val parentRouter = parentRouterRef.get()
        val listener = OnInflateFinishedListener(callback)

        when {
            (parentView != null) -> {
                AsyncLayoutInflater(parentView.context)
                    .inflate(layoutId, parentView, listener)
            }
            (parentRouter != null) -> {
                val provider = ScopeProvider { Completable
                    .timer(CREATE_VIEW_TIMEOUT, TimeUnit.SECONDS) }

                parentRouter.view
                    .take(TAKE_FROM_PARENT)
                    .observeOn(AndroidSchedulers.mainThread())
                    .autoDisposable(provider)
                    .subscribe {
                        AsyncLayoutInflater(it.context)
                            .inflate(layoutId, it as ViewGroup, listener)
                    }
            }
            (context != null) -> {
                AsyncLayoutInflater(context)
                    .inflate(layoutId, null, listener)
            }
            else -> {
                throw IllegalArgumentException(
                    "context = null, parentView = null, parentRouter = null"
                )
            }
        }
    }

    interface Callback {
        fun onInflateFinished(view: View)
    }

    private class OnInflateFinishedListener(
        private val callback: Callback
    ) : AsyncLayoutInflater.OnInflateFinishedListener {
        override fun onInflateFinished(view: View, resid: Int, parent: ViewGroup?) {
            callback.onInflateFinished(view)
        }
    }

    companion object {
        private const val CREATE_VIEW_TIMEOUT = 2L
        private const val TAKE_FROM_PARENT = 1L
    }
}