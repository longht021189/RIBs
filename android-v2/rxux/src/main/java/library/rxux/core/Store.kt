package library.rxux.core

import com.uber.autodispose.autoDisposable
import io.reactivex.subjects.BehaviorSubject

abstract class Store {

    private var scopeProvider: StoreScopeProvider? = null
    private val children by lazy {
        ArrayList<Store>()
    }
    private val addChildEventSubject by lazy {
        BehaviorSubject.createDefault(Unit)
    }
    private var state = StoreState.Detached

    @Synchronized
    fun addChild(child: Store) {
        children.add(child)
        addChildEventSubject.onNext(Unit)
    }

    @Synchronized
    fun dispatchAttach() {
        state = StoreState.Attached
        scopeProvider = StoreScopeProvider().apply {
            dispatchAttach()

            addChildEventSubject
                .autoDisposable(this)
                .subscribe { dispatchAttachToChildren() }
        }
    }

    @Synchronized
    private fun dispatchAttachToChildren() {
        children.forEach { child ->
            if (child.state == StoreState.Detached) {
                child.dispatchAttach()
            }
        }
    }

    @Synchronized
    private fun dispatchDetachToChildren() {
        children.forEach { child ->
            if (child.state == StoreState.Attached) {
                child.dispatchDetach()
            }
        }
    }

    @Synchronized
    fun dispatchDetach() {
        state = StoreState.Detached

        scopeProvider?.dispatchDetach()
        scopeProvider = null

        dispatchDetachToChildren()
    }
}