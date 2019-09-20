package com.uber.rib.corev2

import android.content.Intent
import android.os.Bundle
import com.uber.autodispose.ScopeProvider

interface SimpleInteractor {
    fun didBecomeActive(bundle: Bundle?, provider: ScopeProvider)
    fun willResignActive()

    fun onSaveInstanceState(bundle: Bundle)

    fun handleBackPress(): Boolean
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean
}