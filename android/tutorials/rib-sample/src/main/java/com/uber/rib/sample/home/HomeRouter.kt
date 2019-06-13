package com.uber.rib.sample.home

@HomeBuilder.HomeScope
class HomeRouter @javax.inject.Inject constructor(
    interactor: HomeInteractor,
    private val injector: dagger.android.DispatchingAndroidInjector<Any>
) : com.uber.rib.core.RouterBase<HomeInteractor>(interactor)
