package com.uber.rib.sample.root

import android.content.Context
import android.util.AttributeSet
import android.view.View

class RootView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle), RootInteractor.RootPresenter
