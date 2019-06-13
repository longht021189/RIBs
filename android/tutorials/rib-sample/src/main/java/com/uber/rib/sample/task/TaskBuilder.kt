package com.uber.rib.sample.task

class TaskBuilder {

  @javax.inject.Inject lateinit var router: TaskRouter

  fun build(injector: dagger.android.DispatchingAndroidInjector<Any>): TaskRouter {
    injector.inject(this)
    return router
  }

  @dagger.Module
  interface ParentModule {
    @TaskScope
    @dagger.android.ContributesAndroidInjector(modules = [Module::class])
    fun contributeTaskBuilderInjector(): TaskBuilder
  }

  @dagger.Module
  abstract class Module {

    @dagger.Binds @TaskScope
    abstract fun bindsTaskPresenter(view: TaskView): TaskInteractor.TaskPresenter

    @dagger.Module
    companion object {

      @dagger.Provides
      @TaskScope
      @JvmStatic
      fun inflateTaskView(): TaskView {
        TODO("Inflate a new view using the provided inflater, or create a new view programatically using the provided context from the parentViewGroup.")
      }
    }

    // TODO: Create provider methods for dependencies created by this Rib. These methods should be static.
  }

  @javax.inject.Scope
  @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
  internal annotation class TaskScope
}
