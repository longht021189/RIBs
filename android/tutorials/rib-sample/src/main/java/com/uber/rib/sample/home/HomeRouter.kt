package com.uber.rib.sample.home

@HomeBuilder.HomeScope
class HomeRouter @javax.inject.Inject constructor(
    view: HomeView,
    interactor: HomeInteractor,
    val injector: dagger.android.DispatchingAndroidInjector<Any>
) : com.uber.rib.core.ViewRouterSubcomponent<HomeView, HomeInteractor>(view, interactor)
