package com.uber.rib.sample.ribs.root

import android.view.ViewGroup
import com.uber.rib.core.Router
import com.uber.rib.core.ViewRouterSubcomponent
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider

@RootBuilder.RootScope
class RootRouter @Inject constructor(
    @RootBuilder.RootQualifier
    view: ViewGroup,
    interactor: RootInteractor,
    private val routersLazy: Lazy<Map<String, @JvmSuppressWildcards Provider<Router<*>>>>
) : ViewRouterSubcomponent<ViewGroup, RootInteractor>(view, interactor)
