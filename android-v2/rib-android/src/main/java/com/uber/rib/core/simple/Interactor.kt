package com.uber.rib.core.simple

import android.os.Bundle
import com.uber.autodispose.ScopeProvider
import com.uber.rib.core.SimpleInteractor

abstract class Interactor : SimpleInteractor {

    open fun didBecomeActive(bundle: Bundle?, provider: ScopeProvider) { }

    open fun handleBackPress(): Boolean = false

    open fun onSaveInstanceState(bundle: Bundle) { }

    open fun willResignActive() { }
}