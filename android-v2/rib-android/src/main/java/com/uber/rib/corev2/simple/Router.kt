package com.uber.rib.corev2.simple

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.collection.ArraySet
import com.uber.rib.corev2.SimpleRouter

abstract class Router(
    override val interactor: Interactor
) : SimpleRouter {

    private val helper by lazy {
        InteractorHelper()
    }
    private val children by lazy {
        ArraySet<SimpleRouter>()
    }
    private var state: Bundle? = null

    @CallSuper override fun dispatchAttach(savedInstanceState: Bundle?) {
        helper.dispatchAttach()
        interactor.didBecomeActive(savedInstanceState, helper)
        state = savedInstanceState

        for (child in children) {
            attachChild(child, false)
        }
    }

    override fun attachChild(child: SimpleRouter) {
        attachChild(child, true)
    }

    override fun detachChild(child: SimpleRouter) {
        detachChild(child, true)
    }

    override fun handleBackPress(): Boolean {
        for (child in children) {
            if (child.handleBackPress()) {
                return true
            }
        }

        return interactor.handleBackPress()
    }

    override fun saveInstanceState(outState: Bundle) {
        for (child in children) {
            child.saveInstanceState(outState)
        }

        interactor.onSaveInstanceState(outState)
    }

    @CallSuper override fun dispatchDetach() {
        for (child in children) {
            detachChild(child, false)
        }

        state = null
        interactor.willResignActive()
        helper.dispatchDetach()
    }

    private fun attachChild(child: SimpleRouter, isAdd: Boolean) {
        if (isAdd) {
            children.add(child)
        }

        if (children.contains(child)) {
            child.dispatchAttach(state)
        }
    }

    private fun detachChild(child: SimpleRouter, isRemove: Boolean) {
        if (children.contains(child)) {
            if (isRemove) {
                children.remove(child)
            }

            child.dispatchDetach()
        }
    }
}