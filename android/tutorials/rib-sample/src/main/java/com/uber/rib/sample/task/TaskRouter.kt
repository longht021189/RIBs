package com.uber.rib.sample.task

@TaskBuilder.TaskScope
class TaskRouter @javax.inject.Inject constructor(
    view: TaskView,
    interactor: TaskInteractor,
    val injector: dagger.android.DispatchingAndroidInjector<Any>
) : com.uber.rib.core.ViewRouterSubcomponent<TaskView, TaskInteractor>(view, interactor)
