package com.uber.rib.sample.task

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TaskInteractorTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var presenter: TaskInteractor.TaskPresenter
  @Mock internal lateinit var router: TaskRouter

  private var interactor: TaskInteractor? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    interactor = TestTaskInteractor.create(presenter)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use InteractorHelper to drive your interactor's lifecycle.
    InteractorHelper.attach<TaskInteractor.TaskPresenter, TaskRouter>(interactor!!, presenter, router, null)
    InteractorHelper.detach(interactor!!)

    throw RuntimeException("Remove this test and add real tests.")
  }
}