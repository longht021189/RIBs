package com.uber.rib.sample.root.home

import android.view.View
import com.uber.rib.core.Router
import com.uber.rib.sample.MainActivity
import com.uber.rib.sample.NavigationUtil
import dagger.Provides
import dagger.android.DispatchingAndroidInjector
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import javax.inject.Inject

class HomeBuilder private constructor(injector: DispatchingAndroidInjector<Any>) {

    @Inject internal lateinit var router: HomeRouter

    init {
        injector.inject(this)
    }

    @dagger.Module
    abstract class ParentModule {

        @HomeScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
        abstract fun contributeHomeBuilderInjector(): HomeBuilder

        @dagger.Module
        companion object {

            @JvmStatic @Provides
            @StringKey("HOME") @IntoMap
            fun provideHomeBuilder(injector: DispatchingAndroidInjector<Any>): Router<*> {
                return HomeBuilder(injector).router
            }
        }
    }

    @dagger.Module
    internal abstract class Module {

        @HomeScope
        @dagger.Binds
        internal abstract fun presenter(view: HomeView): HomeInteractor.HomePresenter

        @HomeScope
        @HomeQualifier
        @dagger.Binds
        internal abstract fun bindHomeView(view: HomeView): View

        @dagger.Module
        companion object {

            @HomeScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateHomeView(activity: MainActivity, navigation: NavigationUtil): HomeView {
                return HomeView(activity, navigation)
            }
        }
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class HomeScope

    @javax.inject.Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class HomeQualifier
}
