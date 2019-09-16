package com.uber.rib.core

import android.os.Bundle

interface SimpleRouter {
    val interactor: SimpleInteractor

    fun dispatchAttach(savedInstanceState: Bundle?)
    fun handleBackPress(): Boolean
    fun saveInstanceState(outState: Bundle)
    fun dispatchDetach()

    fun attachChild(child: SimpleRouter)
    fun detachChild(child: SimpleRouter)
}