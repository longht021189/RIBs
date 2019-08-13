package com.uber.rib.sample

import com.uber.rib.sample.ribs.root.RootBuilder
import dagger.Module

@Module(includes = [RootBuilder.ParentModule::class])
abstract class MainActivityModule