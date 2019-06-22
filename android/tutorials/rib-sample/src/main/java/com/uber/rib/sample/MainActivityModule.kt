package com.uber.rib.sample

import com.uber.rib.sample.dialog.MyDialog
import com.uber.rib.sample.dialog.MyDialogBuilder
import com.uber.rib.sample.root.RootBuilder
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton
import com.uber.rib.core.navigation.Navigation as Base

@Module(includes = [RootBuilder.ParentModule::class])
abstract class MainActivityModule {

    @Binds
    @Singleton
    abstract fun bindNavigation(navigation: Navigation): Base

    @ContributesAndroidInjector(modules = [MyDialogBuilder.ParentModule::class])
    @MyDialog.MyDialogScope
    abstract fun contributeMyDialog(): MyDialog
}