package com.uber.rib.sample.root

import com.uber.rib.core.Router
import com.uber.rib.core.ViewRouterSubcomponent
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider

@RootBuilder.RootScope
class RootRouter @Inject constructor(
    view: RootView,
    interactor: RootInteractor,
    private val routersLazy: Lazy<Map<String, @JvmSuppressWildcards Provider<Router<*>>>>
) : ViewRouterSubcomponent<RootView, RootInteractor>(view, interactor)
