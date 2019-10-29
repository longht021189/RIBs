package library.core.redux

import com.uber.autodispose.autoDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class Reducer<Action, State> {
    private var scopeProvider: ReducerScopeProvider? = null
    private val children by lazy {
        ArrayList<Reducer<*, *>>()
    }
    private val addChildEventSubject by lazy {
        BehaviorSubject.createDefault(Unit)
    }

    var routerState = ReducerState.Detached
        private set

    @Synchronized
    fun addChild(child: Reducer<*, *>) {
        children.add(child)
        addChildEventSubject.onNext(Unit)
    }

    @Synchronized
    fun dispatchAttach() {
        routerState = ReducerState.Attached
        scopeProvider = ReducerScopeProvider().apply {
            dispatchAttach()

            addChildEventSubject
                .autoDisposable(this)
                .subscribe { dispatchAttachToChildren() }
        }
    }

    @Synchronized
    private fun dispatchAttachToChildren() {
        children.forEach { child ->
            if (child.routerState == ReducerState.Detached) {
                child.dispatchAttach()
            }
        }
    }

    @Synchronized
    private fun dispatchDetachToChildren() {
        children.forEach { child ->
            if (child.routerState == ReducerState.Attached) {
                child.dispatchDetach()
            }
        }
    }

    @Synchronized
    fun dispatchDetach() {
        routerState = ReducerState.Detached

        scopeProvider?.dispatchDetach()
        scopeProvider = null

        dispatchDetachToChildren()
    }
}