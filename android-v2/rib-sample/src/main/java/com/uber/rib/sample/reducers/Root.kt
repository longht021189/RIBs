package com.uber.rib.sample.reducers

import library.annotation.redux.ToStore
import library.core.redux.Reducer

@ToStore
class Root<B> : Reducer<B, Int>() {
}