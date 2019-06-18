package com.uber.rib.sample.splash

import android.content.Context
import android.util.AttributeSet
import android.view.View

class SplashView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : View(context, attrs, defStyle), SplashInteractor.SplashPresenter
