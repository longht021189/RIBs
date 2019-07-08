package com.uber.rib.sample.root.splash

import android.view.View
import com.uber.rib.core.Router
import com.uber.rib.sample.MainActivity
import dagger.Provides
import dagger.android.DispatchingAndroidInjector
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Inject

class SplashBuilder private constructor(injector: DispatchingAndroidInjector<Any>) {

    @Inject internal lateinit var router: SplashRouter

    init {
        injector.inject(this)
    }

    @dagger.Module
    abstract class ParentModule {

        @SplashScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
        abstract fun contributeSplashBuilderInjector(): SplashBuilder

        @dagger.Module
        companion object {

            @JvmStatic @Provides
            @StringKey("SPLASH") @IntoMap
            fun provideSplashBuilder(injector: DispatchingAndroidInjector<Any>): Router<*> {
                return SplashBuilder(injector).router
            }
        }
    }

    @dagger.Module
    internal abstract class Module {

        @SplashScope
        @dagger.Binds
        internal abstract fun presenter(view: SplashView): SplashInteractor.SplashPresenter

        @SplashScope
        @SplashQualifier
        @dagger.Binds
        internal abstract fun bindSplashView(view: SplashView): View

        @dagger.Module
        companion object {

            @SplashScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateSplashView(activity: MainActivity): SplashView {
                return SplashView(activity)
            }
        }
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class SplashScope

    @javax.inject.Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class SplashQualifier
}
