package com.uber.rib.sample.splash

@SplashBuilder.SplashScope
class SplashRouter @javax.inject.Inject constructor(
    view: SplashView,
    interactor: SplashInteractor,
    val injector: dagger.android.DispatchingAndroidInjector<Any>
) : com.uber.rib.core.ViewRouterSubcomponent<SplashView, SplashInteractor>(view, interactor)
