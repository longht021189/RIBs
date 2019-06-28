package com.uber.rib.core

import dagger.android.DispatchingAndroidInjector

interface IBuilder<T: Router<*>> {
    fun build(injector: DispatchingAndroidInjector<Any>): T
}
