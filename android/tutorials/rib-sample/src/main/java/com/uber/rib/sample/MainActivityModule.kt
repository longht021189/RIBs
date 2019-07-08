package com.uber.rib.sample

import com.uber.rib.core.navigation.Navigation
import com.uber.rib.sample.root.RootBuilder
import com.uber.rib.sample.scope.ActivityScope
import dagger.Binds
import dagger.Module

@Module(includes = [RootBuilder.ParentModule::class])
abstract class MainActivityModule {

    @Binds @ActivityScope
    abstract fun bindNavigation(navigation: NavigationUtil): Navigation
}