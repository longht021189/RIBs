package com.uber.rib.sample.task

import android.content.Context
import android.util.AttributeSet
import android.view.View

class TaskView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyle: Int = 0
) : View(context, attrs, defStyle), TaskInteractor.TaskPresenter
