package com.uber.rib.sample.dialog

@MyDialogBuilder.MyDialogScope
class MyDialogInteractor @javax.inject.Inject constructor(
  presenter: dagger.Lazy<MyDialogPresenter>,
  router: dagger.Lazy<MyDialogRouter>
) : com.uber.rib.core.Interactor<MyDialogInteractor.MyDialogPresenter, MyDialogRouter>(presenter, router) {

  override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
    super.didBecomeActive(savedInstanceState)

    // TODO: Add attachment logic here (RxSubscriptions, etc.).
  }

  override fun willResignActive() {
    super.willResignActive()

    // TODO: Perform any required clean up here, or delete this method entirely if not needed.
  }

  interface MyDialogPresenter
}
