package library.rxux.core

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import io.reactivex.CompletableSource
import io.reactivex.Observable

class StoreScopeProvider : LifecycleScopeProvider<StoreState> {

    private val behaviorRelay: BehaviorRelay<StoreState> by lazy {
        BehaviorRelay.create<StoreState>()
    }
    private val lifecycleRelay: Relay<StoreState> by lazy {
        behaviorRelay.toSerialized()
    }

    fun dispatchAttach() {
        lifecycleRelay.accept(StoreState.Attached)
    }

    fun dispatchDetach() {
        lifecycleRelay.accept(StoreState.Detached)
    }

    override fun lifecycle(): Observable<StoreState> {
        return lifecycleRelay.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<StoreState> {
        return LIFECYCLE_MAP_FUNCTION
    }

    override fun peekLifecycle(): StoreState? {
        return behaviorRelay.value
    }

    override fun requestScope(): CompletableSource {
        return LifecycleScopes.resolveScopeFromLifecycle(this)
    }

    companion object {
        private val LIFECYCLE_MAP_FUNCTION by lazy {
            CorrespondingEventsFunction<StoreState> {
                when (it) {
                    StoreState.Attached -> StoreState.Detached
                    else -> throw LifecycleEndedException()
                }
            }
        }
    }
}