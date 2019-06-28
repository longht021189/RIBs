package com.uber.rib.sample.root

import com.uber.rib.core.Router
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
    abstract class  ParentModule {

        @RootScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
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

        @dagger.Module
        companion object {

            @RootScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateRootView(): RootView {
                TODO("Inflate a new view using the provided inflater, or create a new view programatically using the provided context from the parentViewGroup.")
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class RootScope
}
