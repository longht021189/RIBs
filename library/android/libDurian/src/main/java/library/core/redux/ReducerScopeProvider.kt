package library.core.redux

import com.jakewharton.rxrelay2.BehaviorRelay
import com.jakewharton.rxrelay2.Relay
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import com.uber.autodispose.lifecycle.LifecycleScopes
import io.reactivex.CompletableSource
import io.reactivex.Observable

class ReducerScopeProvider : LifecycleScopeProvider<ReducerState> {

    private val behaviorRelay: BehaviorRelay<ReducerState> by lazy {
        BehaviorRelay.create<ReducerState>()
    }
    private val lifecycleRelay: Relay<ReducerState> by lazy {
        behaviorRelay.toSerialized()
    }

    fun dispatchAttach() {
        lifecycleRelay.accept(ReducerState.Attached)
    }

    fun dispatchDetach() {
        lifecycleRelay.accept(ReducerState.Detached)
    }

    override fun lifecycle(): Observable<ReducerState> {
        return lifecycleRelay.hide()
    }

    override fun correspondingEvents(): CorrespondingEventsFunction<ReducerState> {
        return LIFECYCLE_MAP_FUNCTION
    }

    override fun peekLifecycle(): ReducerState? {
        return behaviorRelay.value
    }

    override fun requestScope(): CompletableSource {
        return LifecycleScopes.resolveScopeFromLifecycle(this)
    }

    companion object {
        private val LIFECYCLE_MAP_FUNCTION by lazy {
            CorrespondingEventsFunction<ReducerState> {
                when (it) {
                    ReducerState.Attached -> ReducerState.Detached
                    else -> throw LifecycleEndedException()
                }
            }
        }
    }
}