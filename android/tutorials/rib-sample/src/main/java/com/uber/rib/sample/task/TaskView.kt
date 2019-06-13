package com.uber.rib.sample.task

class TaskView @JvmOverloads constructor(
    context: android.content.Context,
    attrs: android.util.AttributeSet? = null,
    defStyle: Int = 0
) : android.view.View(context, attrs, defStyle), TaskInteractor.TaskPresenter
