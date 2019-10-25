package library.rxux.core

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import io.reactivex.CompletableSource
import io.reactivex.Observable

class RouterScopeProvider : LifecycleScopeProvider<RouterState> {

    private val behaviorRelay: BehaviorRelay<RouterState> by lazy {
        BehaviorRelay.create<RouterState>()
    }
    private val lifecycleRelay: Relay<RouterState> by lazy {
        behaviorRelay.toSerialized()
    }

    fun dispatchAttach() {
        lifecycleRelay.accept(RouterState.Attached)
    }

    fun dispatchDetach() {
        lifecycleRelay.accept(RouterState.Detached)
    }

    override fun lifecycle(): Observable<RouterState> {
        return lifecycleRelay.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<RouterState> {
        return LIFECYCLE_MAP_FUNCTION
    }

    override fun peekLifecycle(): RouterState? {
        return behaviorRelay.value
    }

    override fun requestScope(): CompletableSource {
        return LifecycleScopes.resolveScopeFromLifecycle(this)
    }

    companion object {
        private val LIFECYCLE_MAP_FUNCTION by lazy {
            CorrespondingEventsFunction<RouterState> {
                when (it) {
                    RouterState.Attached -> RouterState.Detached
                    else -> throw LifecycleEndedException()
                }
            }
        }
    }
}