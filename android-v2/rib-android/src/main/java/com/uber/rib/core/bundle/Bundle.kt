package com.uber.rib.core.bundle

import android.os.Bundle as AndroidBundle

class Bundle {

    private val data: AndroidBundle
    private val key: String?

    constructor(key: String?, parent: AndroidBundle) {
        this.key = key
        this.data = parent.getBundle(key) ?: AndroidBundle()
    }

    constructor(key: String?, parent: Bundle) {
        this.key = key
        this.data = parent.getBundle(key) ?: AndroidBundle()
    }

    fun getBundle(key: String?): AndroidBundle? {
        return data.getBundle(key)
    }

    fun putBundle(key: String?, bundle: AndroidBundle?) {
        data.putBundle(key, bundle)
    }

    fun putBundle(bundle: Bundle?) {
        bundle?.let {
            data.putBundle(it.key, it.data)
        }
    }

    fun putString(key: String?, value: String?) {
        data.putString(key, value)
    }

    fun putFloat(key: String?, value: Float) {
        data.putFloat(key, value)
    }

    fun putInt(key: String?, value: Int) {
        data.putInt(key, value)
    }

    fun putDouble(key: String?, value: Double) {
        data.putDouble(key, value)
    }
}