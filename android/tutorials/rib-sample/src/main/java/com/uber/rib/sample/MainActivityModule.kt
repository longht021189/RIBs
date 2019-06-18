package com.uber.rib.sample

import com.uber.rib.sample.root.RootBuilder
import dagger.Module

@Module(includes = [RootBuilder.ParentModule::class])
abstract class MainActivityModule