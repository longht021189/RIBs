package com.uber.rib.core

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.DialogFragment
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.PublishRelay
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import com.uber.rib.core.lifecycle.ActivityCallbackEvent
import com.uber.rib.core.lifecycle.ActivityLifecycleEvent
import io.reactivex.CompletableSource
import io.reactivex.Observable
import com.uber.rib.core.Bundle as RibBundle

abstract class RibDialogFragment : DialogFragment(), ActivityStarter,
    LifecycleScopeProvider<ActivityLifecycleEvent>, RxActivityEvents {

    private var router: IViewRouter<*>? = null

    private val lifecycleBehaviorRelay = BehaviorRelay.create<ActivityLifecycleEvent>()
    private val lifecycleRelay = lifecycleBehaviorRelay.toSerialized()
    private val callbacksRelay = PublishRelay.create<ActivityCallbackEvent>().toSerialized()

    protected val interactor: Interactor<*, *> get() {
        return router?.interactor ?: throw IllegalStateException(
            "Attempting to get a router when activity is not created or has been " + "destroyed."
        )
    }

    override fun lifecycle(): Observable<ActivityLifecycleEvent> = lifecycleRelay.hide()

    override fun callbacks(): Observable<ActivityCallbackEvent> = callbacksRelay.hide()

    override fun correspondingEvents() = FRAGMENT_LIFECYCLE

    override fun peekLifecycle(): ActivityLifecycleEvent? {
        return lifecycleBehaviorRelay.value
    }

    override fun requestScope(): CompletableSource {
        return LifecycleScopes.resolveScopeFromLifecycle(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let { RibDialog(it, theme,
            this) } ?: super.onCreateDialog(savedInstanceState)
    }

    open fun onCreateDialog(dialog: Dialog) {}

    @CallSuper
    @Initializer
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycleRelay.accept(ActivityLifecycleEvent.createOnCreateEvent(savedInstanceState))

        val router = createRouter(container)
        var wrappedBundle: RibBundle? = null

        if (savedInstanceState != null) {
            wrappedBundle = RibBundle(savedInstanceState)
        }

        router.dispatchAttach(wrappedBundle)

        this.router = router
        return router.view
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        callbacksRelay.accept(ActivityCallbackEvent
            .createOnSaveInstanceStateEvent(outState))

        Preconditions.checkNotNull(router)
            .saveInstanceState(RibBundle(outState))
    }

    @CallSuper
    override fun onStart() {
        super.onStart()

        lifecycleRelay.accept(ActivityLifecycleEvent
            .create(ActivityLifecycleEvent.Type.START))
    }

    @CallSuper
    override fun onResume() {
        super.onResume()

        lifecycleRelay.accept(ActivityLifecycleEvent
            .create(ActivityLifecycleEvent.Type.RESUME))
    }

    @CallSuper
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbacksRelay.accept(ActivityCallbackEvent
            .createOnActivityResultEvent(requestCode, resultCode, data))
    }

    @CallSuper
    override fun onPause() {
        lifecycleRelay.accept(ActivityLifecycleEvent
            .create(ActivityLifecycleEvent.Type.PAUSE))

        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleRelay.accept(ActivityLifecycleEvent
            .create(ActivityLifecycleEvent.Type.STOP))

        super.onStop()
    }

    @CallSuper
    override fun onDestroyView() {
        lifecycleRelay.accept(ActivityLifecycleEvent
            .create(ActivityLifecycleEvent.Type.DESTROY))

        router?.dispatchDetach()
        router = null

        super.onDestroyView()
    }

    @CallSuper
    override fun onLowMemory() {
        super.onLowMemory()

        callbacksRelay.accept(ActivityCallbackEvent
            .create(ActivityCallbackEvent.Type.LOW_MEMORY))
    }

    override fun dismiss() {
        if (router?.handleBackPress() == false) {
            super.dismiss()
        }
    }

    protected abstract fun createRouter(parentViewGroup: ViewGroup?): IViewRouter<*>

    class RibDialog(
        context: Context, themeResId: Int,
        private val fragment: RibDialogFragment
    ) : Dialog(context, themeResId) {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            fragment.onCreateDialog(this)
        }
    }

    companion object {
        private val FRAGMENT_LIFECYCLE by lazy {
            CorrespondingEventsFunction<ActivityLifecycleEvent> { lastEvent ->
                when (lastEvent.type) {
                    ActivityLifecycleEvent.Type.CREATE -> {
                        ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.DESTROY)
                    }
                    ActivityLifecycleEvent.Type.START -> {
                        ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.STOP)
                    }
                    ActivityLifecycleEvent.Type.RESUME -> {
                        ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.PAUSE)
                    }
                    ActivityLifecycleEvent.Type.PAUSE -> {
                        ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.STOP)
                    }
                    ActivityLifecycleEvent.Type.STOP -> {
                        ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.DESTROY)
                    }
                    ActivityLifecycleEvent.Type.DESTROY -> {
                        throw LifecycleEndedException(
                            "Cannot bind to Activity lifecycle when outside of it."
                        )
                    }
                    else -> {
                        throw UnsupportedOperationException("Binding to $lastEvent not yet implemented")
                    }
                }
            }
        }
    }
}
