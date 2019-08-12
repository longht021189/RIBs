package com.uber.rib.sample.test

import com.uber.rib.core.Router
import dagger.Lazy
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Provider

@TestBuilder.TestScope
class TestRouter @Inject constructor(
    interactor: TestInteractor,
    private val presenter: TestPresenter,
    private val routersLazy: Lazy<Map<String, @JvmSuppressWildcards Provider<Router<*>>>>
) : Router<TestInteractor>(interactor) {

    val view: Observable<TestView> get() {
        return presenter.view
    }
}
