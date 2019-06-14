package com.uber.rib.sample.home

class HomeBuilder {

    @javax.inject.Inject lateinit var router: HomeRouter

    fun build(injector: dagger.android.DispatchingAndroidInjector<Any>): HomeRouter {
        injector.inject(this)
        return router
    }

    @dagger.Module
    interface ParentModule {

        @HomeScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
        fun contributeHomeBuilderInjector(): HomeBuilder
    }

    @dagger.Module
    internal abstract class Module {
        // TODO: Create provider methods for dependencies created by this Rib. These methods should be static.
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class HomeScope

    @HomeScope
    class HomePresenter @javax.inject.Inject constructor(): com.uber.rib.core.EmptyPresenter()
}
