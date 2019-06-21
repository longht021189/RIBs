package com.uber.rib.sample

import com.uber.rib.sample.root.RootBuilder
import dagger.Module
import dagger.Binds
import javax.inject.Singleton
import com.uber.rib.core.navigation.Navigation as Base

@Module(includes = [RootBuilder.ParentModule::class])
abstract class MainActivityModule {

    @Binds
    @Singleton
    abstract fun bindNavigation(navigation: Navigation): Base
}