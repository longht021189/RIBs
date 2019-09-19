package com.uber.rib.corev2.simple

import android.os.Bundle
import com.uber.autodispose.ScopeProvider
import com.uber.rib.corev2.SimpleInteractor

abstract class Interactor : SimpleInteractor {

    open fun didBecomeActive(bundle: Bundle?, provider: ScopeProvider) { }

    open fun handleBackPress(): Boolean = false

    open fun onSaveInstanceState(bundle: Bundle) { }

    open fun willResignActive() { }
}