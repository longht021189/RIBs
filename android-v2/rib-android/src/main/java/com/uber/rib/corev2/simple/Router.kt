package com.uber.rib.corev2.simple

import android.content.Intent
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.collection.ArraySet
import com.uber.rib.corev2.SimpleRouter
import com.uber.autodispose.ScopeProvider
import java.util.*

abstract class Router(
    override val interactor: Interactor
) : SimpleRouter {

    private val helper by lazy {
        InteractorHelper()
    }
    private val childrenStack by lazy {
        Stack<LinkedList<SimpleRouter>>()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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