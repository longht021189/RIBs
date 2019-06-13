package com.uber.rib.sample.root

import android.view.LayoutInflater
import android.view.ViewGroup
import com.uber.rib.core.InteractorBaseComponent
import com.uber.rib.core.ViewBuilder
import com.uber.rib.sample.home.HomeBuilder
import com.uber.rib.sample.task.TaskBuilder
import dagger.Binds
import dagger.BindsInstance
import dagger.Provides
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy.CLASS
import javax.inject.Qualifier
import javax.inject.Scope

/**
 * Builder for the {@link RootScope}.
 *
 * TODO describe this scope's responsibility as a whole.
 */
class RootBuilder(dependency: ParentComponent) : ViewBuilder<RootView, RootRouter, RootBuilder.ParentComponent>(dependency) {

  /**
   * Builds a new [RootRouter].
   *
   * @param parentViewGroup parent view group that this router's view will be added to.
   * @return a new [RootRouter].
   */
  fun build(parentViewGroup: ViewGroup): RootRouter {
    val view = createView(parentViewGroup)
    val interactor = RootInteractor()
    val component = DaggerRootBuilder_Component.builder()
        .parentComponent(dependency)
        .view(view)
        .interactor(interactor)
        .build()
    return component.rootRouter()
  }

  override fun inflateView(inflater: LayoutInflater, parentViewGroup: ViewGroup): RootView? {
    // TODO: Inflate a new view using the provided inflater, or create a new view programatically using the
    // provided context from the parentViewGroup.
    return null
  }

  interface ParentComponent {
    // TODO: Define dependencies required from your parent interactor here.
  }

  @dagger.Module
  abstract class Module {

    @RootScope
    @Binds
    internal abstract fun presenter(view: RootView): RootInteractor.RootPresenter

    @dagger.Module
    companion object {

      @RootScope
      @Provides
      @JvmStatic
      internal fun router(
          component: Component,
          view: RootView,
          interactor: RootInteractor,
          injector: dagger.android.DispatchingAndroidInjector<Any>): RootRouter {
        return RootRouter(view, interactor, component, injector)
      }
    }

    // TODO: Create provider methods for dependencies created by this Rib. These should be static.
  }

  @RootScope
  @dagger.Component(modules = [
    Module::class, HomeBuilder.ParentModule::class,
    AndroidInjectionModule::class, AndroidSupportInjectionModule::class,
    TaskBuilder.ParentModule::class
  ], dependencies = arrayOf(ParentComponent::class))
  interface Component : InteractorBaseComponent<RootInteractor>, BuilderComponent {

    @dagger.Component.Builder
    interface Builder {
      @BindsInstance
      fun interactor(interactor: RootInteractor): Builder

      @BindsInstance
      fun view(view: RootView): Builder

      fun parentComponent(component: ParentComponent): Builder
      fun build(): Component
    }
  }

  interface BuilderComponent {
    fun rootRouter(): RootRouter
  }

  @Scope
  @Retention(CLASS)
  internal annotation class RootScope

  @Qualifier
  @Retention(CLASS)
  internal annotation class RootInternal
}
