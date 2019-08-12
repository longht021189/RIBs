package com.uber.rib.android.lazy

import android.view.View
import io.reactivex.Observable

interface IPresenterLazy<T: View> {
    val view: Observable<T>
}