package com.uber.rib.core

import dagger.android.DispatchingAndroidInjector

interface IBuilder {
    fun build(injector: DispatchingAndroidInjector<Any>)
}
