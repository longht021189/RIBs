package com.uber.rib.sample.splash

import android.graphics.Color
import com.uber.rib.sample.MainActivity

class SplashBuilder {

    @javax.inject.Inject lateinit var router: SplashRouter

    fun build(injector: dagger.android.DispatchingAndroidInjector<Any>): SplashRouter {
        injector.inject(this)
        return router
    }

    @dagger.Module
    interface ParentModule {

        @SplashScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
        fun contributeSplashBuilderInjector(): SplashBuilder
    }

    @dagger.Module
    internal abstract class Module {

        @SplashScope
        @dagger.Binds
        internal abstract fun presenter(view: SplashView): SplashInteractor.SplashPresenter

        @dagger.Module
        companion object {

            @SplashScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateSplashView(activity: MainActivity): SplashView {
                return SplashView(activity).apply {
                    setBackgroundColor(Color.GREEN)
                }
            }
        }
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class SplashScope
}
