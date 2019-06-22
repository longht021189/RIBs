package com.uber.rib.sample.dialog

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class MyDialogView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle), MyDialogInteractor.MyDialogPresenter
