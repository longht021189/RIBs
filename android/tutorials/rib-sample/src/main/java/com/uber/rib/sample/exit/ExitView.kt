package com.uber.rib.sample.exit

import android.content.Context
import android.util.AttributeSet
import android.view.View

class ExitView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : View(context, attrs, defStyle), ExitInteractor.ExitPresenter
