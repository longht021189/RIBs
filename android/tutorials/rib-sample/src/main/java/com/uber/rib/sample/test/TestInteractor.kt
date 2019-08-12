package com.uber.rib.sample.test

import javax.inject.Inject
import dagger.Lazy
import com.uber.rib.core.Interactor
import com.uber.rib.core.Bundle

@TestBuilder.TestScope
class TestInteractor @Inject constructor(
    presenter: Lazy<TestPresenter>,
    router: Lazy<TestRouter>
) : Interactor<TestInteractor.TestPresenter, TestRouter>(presenter, router) {

    interface TestPresenter
}
