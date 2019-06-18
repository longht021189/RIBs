package com.uber.rib.sample.splash

@SplashBuilder.SplashScope
class SplashInteractor @javax.inject.Inject constructor(
  presenter: dagger.Lazy<SplashPresenter>,
  router: dagger.Lazy<SplashRouter>
) : com.uber.rib.core.Interactor<SplashInteractor.SplashPresenter, SplashRouter>(presenter, router) {

  override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
    super.didBecomeActive(savedInstanceState)

    // TODO: Add attachment logic here (RxSubscriptions, etc.).
  }

  override fun willResignActive() {
    super.willResignActive()

    // TODO: Perform any required clean up here, or delete this method entirely if not needed.
  }

  interface SplashPresenter
}
