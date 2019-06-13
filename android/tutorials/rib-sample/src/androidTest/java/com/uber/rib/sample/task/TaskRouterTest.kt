package com.uber.rib.sample.task

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class TaskRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: TaskBuilder.Component
  @Mock internal lateinit var interactor: TaskInteractor
  @Mock internal lateinit var view: TaskView

  private var router: TaskRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = TaskRouter(view, interactor, component)
  }

  /**
   * TODO: Delete this example and add real tests.
   */
  @Test
  fun anExampleTest_withSomeConditions_shouldPass() {
    // Use RouterHelper to drive your router's lifecycle.
    RouterHelper.attach(router!!)
    RouterHelper.detach(router!!)

    throw RuntimeException("Remove this test and add real tests.")
  }

}

