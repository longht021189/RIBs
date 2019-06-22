package com.uber.rib.sample.exit

class ExitBuilder {

    @javax.inject.Inject lateinit var router: ExitRouter

    fun build(injector: dagger.android.DispatchingAndroidInjector<Any>): ExitRouter {
        injector.inject(this)
        return router
    }

    @dagger.Module
    interface ParentModule {

        @ExitScope
        @dagger.android.ContributesAndroidInjector(modules = [Module::class])
        fun contributeExitBuilderInjector(): ExitBuilder
    }

    @dagger.Module
    internal abstract class Module {

        @ExitScope
        @dagger.Binds
        internal abstract fun presenter(view: ExitView): ExitInteractor.ExitPresenter

        @dagger.Module
        companion object {

            @ExitScope
            @dagger.Provides
            @JvmStatic
            internal fun inflateExitView(): ExitView {
                TODO("Inflate a new view using the provided inflater, or create a new view programatically using the provided context from the parentViewGroup.")
            }
        }

        // TODO: Create provider methods for dependencies created by this Rib. These should be static.
    }

    @javax.inject.Scope
    @kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
    internal annotation class ExitScope
}
