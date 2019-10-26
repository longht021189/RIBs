package com.uber.rib.sample

import android.app.Application
import library.rxux.annotation.AutoRouter

@AutoRouter
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}