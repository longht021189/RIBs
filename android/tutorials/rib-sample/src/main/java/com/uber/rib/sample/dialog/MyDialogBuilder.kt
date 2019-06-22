package com.uber.rib.sample.dialog

import com.uber.rib.sample.MainActivity

class MyDialogBuilder {

    @javax.inject.Inject lateinit var router: MyDialogRouter

    fun build(injector: dagger.android.DispatchingAndroidInjector<Any>): MyDialogRouter {
        injector.inject(this)
        return router
    }

    @dagger.Module
    interface ParentModule {

        @MyDialogScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
        fun contributeMyDialogBuilderInjector(): MyDialogBuilder
    }

    @dagger.Module
    internal abstract class Module {

        @MyDialogScope
        @dagger.Binds
        internal abstract fun presenter(view: MyDialogView): MyDialogInteractor.MyDialogPresenter

        @dagger.Module
        companion object {

            @MyDialogScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateMyDialogView(activity: MainActivity): MyDialogView {
                return MyDialogView(activity)
            }
        }
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class MyDialogScope
}
