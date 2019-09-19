package com.uber.rib.corev2.simple

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import com.uber.rib.core.lifecycle.InteractorEvent
import io.reactivex.CompletableSource
import io.reactivex.Observable

class InteractorHelper : LifecycleScopeProvider<InteractorEvent> {

    private val behaviorRelay: BehaviorRelay<InteractorEvent> by lazy {
        BehaviorRelay.create<InteractorEvent>()
    }
    private val lifecycleRelay: Relay<InteractorEvent> by lazy {
        behaviorRelay.toSerialized()
    }

    fun dispatchAttach() {
        lifecycleRelay.accept(InteractorEvent.ACTIVE)
    }

    fun dispatchDetach() {
        lifecycleRelay.accept(InteractorEvent.INACTIVE)
    }

    override fun lifecycle(): Observable<InteractorEvent> {
        return lifecycleRelay.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<InteractorEvent> {
        return LIFECYCLE_MAP_FUNCTION
    }

    override fun peekLifecycle(): InteractorEvent? {
        return behaviorRelay.value
    }

    override fun requestScope(): CompletableSource {
        return LifecycleScopes.resolveScopeFromLifecycle(this)
    }

    companion object {
        private val LIFECYCLE_MAP_FUNCTION by lazy {
            CorrespondingEventsFunction<InteractorEvent> {
                when (it) {
                    InteractorEvent.ACTIVE -> InteractorEvent.INACTIVE
                    else -> throw LifecycleEndedException()
                }
            }
        }
    }
}