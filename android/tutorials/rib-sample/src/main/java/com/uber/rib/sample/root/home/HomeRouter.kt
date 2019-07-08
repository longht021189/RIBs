package com.uber.rib.sample.root.home

import android.view.View
import com.uber.rib.core.Router
import com.uber.rib.core.ViewRouterSubcomponent
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider

@HomeBuilder.HomeScope
class HomeRouter @Inject constructor(
    @HomeBuilder.HomeQualifier
    view: View,
    interactor: HomeInteractor,
    private val routersLazy: Lazy<Map<String, @JvmSuppressWildcards Provider<Router<*>>>>
) : ViewRouterSubcomponent<View, HomeInteractor>(view, interactor)
