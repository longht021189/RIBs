package com.uber.rib.sample.home

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class HomeRouterTest : RibTestBasePlaceholder() {

  @Mock internal lateinit var component: HomeBuilder.Component
  @Mock internal lateinit var interactor: HomeInteractor

  private var router: HomeRouter? = null

  @Before
  fun setup() {
    MockitoAnnotations.initMocks(this)

    router = HomeRouter(interactor, component)
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
