package com.uber.rib.sample

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class, MainActivityModule::class])
@Singleton
interface MainActivityComponent : AndroidInjector<MainActivity> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<MainActivity>
}