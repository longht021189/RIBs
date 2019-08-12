package com.uber.rib.sample.test

import com.uber.rib.core.Interactor
import dagger.Lazy
import javax.inject.Inject

@TestBuilder.TestScope
class TestInteractor @Inject constructor(
    presenter: Lazy<TestPresenter>,
    router: Lazy<TestRouter>
) : Interactor<TestInteractor.TestPresenter, TestRouter>(presenter, router) {

    interface TestPresenter
}
