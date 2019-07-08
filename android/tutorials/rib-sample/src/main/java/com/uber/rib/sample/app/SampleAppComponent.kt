package com.uber.rib.sample.app

import com.uber.rib.sample.MainActivity
import com.uber.rib.sample.MainActivityModule
import com.uber.rib.sample.scope.ActivityScope
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    SampleAppComponent.SampleAppModule::class,
    AndroidInjectionModule::class,
    AndroidSupportInjectionModule::class
])
interface SampleAppComponent : AndroidInjector<SampleApp> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<SampleApp>

    @Module
    interface SampleAppModule {

        @ActivityScope
        @ContributesAndroidInjector(modules = [MainActivityModule::class])
        fun contributeMainActivity(): MainActivity
    }
}