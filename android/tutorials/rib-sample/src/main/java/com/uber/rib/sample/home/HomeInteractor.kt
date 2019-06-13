package com.uber.rib.sample.home

@HomeBuilder.HomeScope
class HomeInteractor @javax.inject.Inject constructor(
  presenter: dagger.Lazy<HomeBuilder.HomePresenter>,
  router: dagger.Lazy<HomeRouter>
) : com.uber.rib.core.InteractorBase<HomeBuilder.HomePresenter, HomeRouter>(presenter, router) {

  override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
    super.didBecomeActive(savedInstanceState)

    // TODO: Add attachment logic here (RxSubscriptions, etc.).
  }

  override fun willResignActive() {
    super.willResignActive()

    // TODO: Perform any required clean up here, or delete this method entirely if not needed.
  }

}
