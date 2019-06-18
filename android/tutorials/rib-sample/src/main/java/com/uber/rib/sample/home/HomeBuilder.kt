package com.uber.rib.sample.home

import android.graphics.Color
import com.uber.rib.sample.MainActivity

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

        @HomeScope
        @dagger.Binds
        internal abstract fun presenter(view: HomeView): HomeInteractor.HomePresenter

        @dagger.Module
        companion object {

            @HomeScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateHomeView(activity: MainActivity): HomeView {
                return HomeView(activity).apply {
                    setBackgroundColor(Color.MAGENTA)
                }
            }
        }
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class HomeScope
}
