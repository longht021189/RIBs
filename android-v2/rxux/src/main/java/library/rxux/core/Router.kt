package library.rxux.core

import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject

abstract class Router<Action, State>(
    initialState: State,
    private val actionSubject: Subject<Action>,
    private val stateSubject: Subject<State>
) {
    private var scopeProvider: RouterScopeProvider? = null
    private val children by lazy {
        ArrayList<Router<*, *>>()
    }
    private val addChildEventSubject by lazy {
        BehaviorSubject.createDefault(Unit)
    }

    var dataState: State = initialState
        private set

    var routerState = RouterState.Detached
        private set

    @Synchronized
    fun addChild(child: Router<*, *>) {
        children.add(child)
        addChildEventSubject.onNext(Unit)
    }

    @Synchronized
    fun dispatchAttach() {
        routerState = RouterState.Attached
        scopeProvider = RouterScopeProvider().apply {
            dispatchAttach()

            addChildEventSubject
                .autoDisposable(this)
                .subscribe { dispatchAttachToChildren() }

            Observable
                .merge(Observable.just(dataState), actionSubject.map { onUpdate(dataState, it) })
                .distinctUntilChanged()
                .autoDisposable(this)
                .subscribe {
                    dataState = it
                    stateSubject.onNext(it)
                }
        }
    }

    @Synchronized
    private fun dispatchAttachToChildren() {
        children.forEach { child ->
            if (child.routerState == RouterState.Detached) {
                child.dispatchAttach()
            }
        }
    }

    @Synchronized
    private fun dispatchDetachToChildren() {
        children.forEach { child ->
            if (child.routerState == RouterState.Attached) {
                child.dispatchDetach()
            }
        }
    }

    @Synchronized
    fun dispatchDetach() {
        routerState = RouterState.Detached

        scopeProvider?.dispatchDetach()
        scopeProvider = null

        dispatchDetachToChildren()
    }

    protected abstract fun onUpdate(
        oldState: State, action: Action): State
}