package com.uber.rib.core.simple

import android.content.Intent
import android.os.Bundle
import com.uber.autodispose.ScopeProvider
import com.uber.rib.core.SimpleInteractor

abstract class Interactor : SimpleInteractor {

    override fun didBecomeActive(bundle: Bundle?, provider: ScopeProvider) = Unit

    override fun handleBackPress(): Boolean = false

    override fun onSaveInstanceState(bundle: Bundle) = Unit

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean = false

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean = false

    override fun willResignActive() = Unit
}