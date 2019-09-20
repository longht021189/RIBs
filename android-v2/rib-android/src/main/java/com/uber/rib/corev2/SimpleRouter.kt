package com.uber.rib.corev2

import android.content.Intent
import android.os.Bundle

interface SimpleRouter {
    val interactor: SimpleInteractor

    fun dispatchAttach(savedInstanceState: Bundle?)
    fun dispatchDetach()

    fun attachChild(child: SimpleRouter)
    fun detachChild(child: SimpleRouter)

    fun handleBackPress(): Boolean
    fun saveInstanceState(outState: Bundle)
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray): Boolean
}