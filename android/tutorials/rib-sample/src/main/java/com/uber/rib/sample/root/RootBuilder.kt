package com.uber.rib.sample.root

import android.view.ViewGroup
import com.uber.rib.core.Router
import com.uber.rib.sample.MainActivity
import com.uber.rib.sample.root.home.HomeBuilder
import com.uber.rib.sample.root.splash.SplashBuilder
import dagger.Provides
import dagger.android.DispatchingAndroidInjector
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Inject

class RootBuilder private constructor(injector: DispatchingAndroidInjector<Any>) {

    @Inject internal lateinit var router: RootRouter

    init {
        injector.inject(this)
    }

    @dagger.Module
    abstract class ParentModule {

        @RootScope
        @dagger.android.ContributesAndroidInjector(modules = [
            HomeBuilder.ParentModule::class,
            SplashBuilder.ParentModule::class,
            Module::class
        ])
        abstract fun contributeRootBuilderInjector(): RootBuilder

        @dagger.Module
        companion object {

            @JvmStatic @Provides
            @StringKey("ROOT") @IntoMap
            fun provideRootBuilder(injector: DispatchingAndroidInjector<Any>): Router<*> {
                return RootBuilder(injector).router
            }
        }
    }

    @dagger.Module
    internal abstract class Module {

        @RootScope
        @dagger.Binds
        internal abstract fun presenter(view: RootView): RootInteractor.RootPresenter

        @RootScope
        @RootQualifier
        @dagger.Binds
        internal abstract fun bindRootViewGroup(view: RootView): ViewGroup

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

    @javax.inject.Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class RootQualifier
}
