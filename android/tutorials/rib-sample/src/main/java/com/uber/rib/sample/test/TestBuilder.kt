package com.uber.rib.sample.test

import android.view.View
import javax.inject.Inject
import dagger.android.DispatchingAndroidInjector
import com.uber.rib.core.Router
import dagger.Provides
import dagger.multibindings.IntoMap
import dagger.multibindings.StringKey
import com.uber.rib.android.lazy.IPresenterLazy

class TestBuilder private constructor(injector: DispatchingAndroidInjector<Any>) {

    @Inject internal lateinit var router: TestRouter

    init {
        injector.inject(this)
    }

    @dagger.Module
    abstract class ParentModule {

        @TestScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
        abstract fun contributeTestBuilderInjector(): TestBuilder

        @dagger.Module
        companion object {

            @JvmStatic @Provides
            @StringKey("TEST") @IntoMap
            fun provideTestBuilder(injector: DispatchingAndroidInjector<Any>): Router<*> {
                return TestBuilder(injector).router
            }
        }
    }

    @dagger.Module
    internal abstract class Module {

        @TestScope
        @dagger.Binds
        internal abstract fun bindsTestPresenterInternal(
            presenter: TestPresenter
        ) : TestInteractor.TestPresenter

        @TestScope
        @TestQualifier
        @dagger.Binds
        internal abstract fun bindTestPresenter(
            presenter: TestPresenter
        ) : IPresenterLazy<View>

        @dagger.Module
        companion object {

            @TestScope
            @dagger.Provides
            @JvmStatic
            internal fun provideTestPresenter(): TestPresenter {
                TODO("Inflate a new view using the provided inflater, or create a new view programatically using the provided context from the parentViewGroup.")
            }
        }
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class TestScope

    @javax.inject.Qualifier
    @kotlin.annotation.Retention(AnnotationRetention.BINARY)
    internal annotation class TestQualifier
}
