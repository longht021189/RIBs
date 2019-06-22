package com.uber.rib.sample.exit

@ExitBuilder.ExitScope
class ExitRouter @javax.inject.Inject constructor(
    view: ExitView,
    interactor: ExitInteractor,
    val injector: dagger.android.DispatchingAndroidInjector<Any>
) : com.uber.rib.core.ViewRouterSubcomponent<ExitView, ExitInteractor>(view, interactor)
