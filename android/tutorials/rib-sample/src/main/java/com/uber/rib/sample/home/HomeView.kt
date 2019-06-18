package com.uber.rib.sample.home

import android.content.Context
import android.util.AttributeSet
import android.view.View

class HomeView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : View(context, attrs, defStyle), HomeInteractor.HomePresenter
