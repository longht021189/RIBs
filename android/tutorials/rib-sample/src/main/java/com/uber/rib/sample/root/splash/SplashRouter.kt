package com.uber.rib.sample.root.splash

import android.view.View
import com.uber.rib.core.Router
import com.uber.rib.core.ViewRouterSubcomponent
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider

@SplashBuilder.SplashScope
class SplashRouter @Inject constructor(
    @SplashBuilder.SplashQualifier
    view: View,
    interactor: SplashInteractor,
    private val routersLazy: Lazy<Map<String, @JvmSuppressWildcards Provider<Router<*>>>>
) : ViewRouterSubcomponent<View, SplashInteractor>(view, interactor)
