package com.uber.rib.sample.test

import com.uber.rib.core.Router
import javax.inject.Inject
import dagger.Lazy
import javax.inject.Provider
import io.reactivex.Observable

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
