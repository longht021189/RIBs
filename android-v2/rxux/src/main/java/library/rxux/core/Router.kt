package library.rxux.core

import com.uber.autodispose.autoDisposable
import io.reactivex.subjects.BehaviorSubject

class Router {

    private var scopeProvider: RouterScopeProvider? = null
    private val children by lazy {
        ArrayList<Router>()
    }
    private val addChildEventSubject by lazy {
        BehaviorSubject.createDefault(Unit)
    }

    var state = RouterState.Detached
        private set

    @Synchronized
    fun addChild(child: Router) {
        children.add(child)
        addChildEventSubject.onNext(Unit)
    }

    @Synchronized
    fun dispatchAttach() {
        state = RouterState.Attached
        scopeProvider = RouterScopeProvider().apply {
            dispatchAttach()

            addChildEventSubject
                .autoDisposable(this)
                .subscribe { dispatchAttachToChildren() }
        }
    }

    @Synchronized
    private fun dispatchAttachToChildren() {
        children.forEach { child ->
            if (child.state == RouterState.Detached) {
                child.dispatchAttach()
            }
        }
    }

    @Synchronized
    private fun dispatchDetachToChildren() {
        children.forEach { child ->
            if (child.state == RouterState.Attached) {
                child.dispatchDetach()
            }
        }
    }

    @Synchronized
    fun dispatchDetach() {
        state = RouterState.Detached

        scopeProvider?.dispatchDetach()
        scopeProvider = null

        dispatchDetachToChildren()
    }
}