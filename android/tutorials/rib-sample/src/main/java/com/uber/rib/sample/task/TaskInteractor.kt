package com.uber.rib.sample.task

@TaskBuilder.TaskScope
class TaskInteractor @javax.inject.Inject constructor(
  presenter: dagger.Lazy<TaskPresenter>,
  router: dagger.Lazy<TaskRouter>
) : com.uber.rib.core.InteractorBase<TaskInteractor.TaskPresenter, TaskRouter>(presenter, router) {

  @javax.inject.Inject @JvmField
  internal var presenter: TaskPresenter? = null

  override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
    super.didBecomeActive(savedInstanceState)

    // TODO: Add attachment logic here (RxSubscriptions, etc.).
  }

  override fun willResignActive() {
    super.willResignActive()

    // TODO: Perform any required clean up here, or delete this method entirely if not needed.
  }

  interface TaskPresenter
}
