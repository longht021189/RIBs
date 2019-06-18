package com.uber.rib.sample.root

import com.uber.rib.sample.MainActivity
import com.uber.rib.sample.home.HomeBuilder
import com.uber.rib.sample.splash.SplashBuilder

class RootBuilder {

    @javax.inject.Inject lateinit var router: RootRouter

    fun build(injector: dagger.android.DispatchingAndroidInjector<Any>): RootRouter {
        injector.inject(this)
        return router
    }

    @dagger.Module
    interface ParentModule {

        @RootScope
        @dagger.android.ContributesAndroidInjector(modules = [
            Module::class, SplashBuilder.ParentModule::class,
            HomeBuilder.ParentModule::class
        ])
        fun contributeRootBuilderInjector(): RootBuilder
    }

    @dagger.Module
    internal abstract class Module {

        @RootScope
        @dagger.Binds
        internal abstract fun presenter(view: RootView): RootInteractor.RootPresenter

        @dagger.Module
        companion object {

            @RootScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateRootView(activity: MainActivity): RootView {
                return RootView(activity)
            }
        }
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class RootScope
}
