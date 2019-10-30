package com.uber.rib.sample.reducers

import com.uber.rib.sample.MainActivity
import library.annotation.redux.ToStore
import library.core.redux.Reducer

@ToStore
class Root(
    private val index: MainActivity,
    private val index2: Float
) : Reducer<Int, Int>() {

    constructor(aa: MainActivity): this(aa, 0f)

    fun abc() {

    }
}