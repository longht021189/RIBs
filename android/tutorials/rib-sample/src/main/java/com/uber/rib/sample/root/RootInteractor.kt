package com.uber.rib.sample.root

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

@RootBuilder.RootScope
class RootInteractor @javax.inject.Inject constructor(
    presenter: dagger.Lazy<RootPresenter>,
    router: dagger.Lazy<RootRouter>
) : com.uber.rib.core.Interactor<RootInteractor.RootPresenter, RootRouter>(presenter, router) {

    private val disposables by lazy {
        CompositeDisposable()
    }

    override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
        super.didBecomeActive(savedInstanceState)
        router.routeToSplash()
        disposables.add(presenter.click.subscribe {
            router.routeToHome()
        })
    }

    override fun willResignActive() {
        disposables.clear()
        super.willResignActive()
    }

    interface RootPresenter {
        val click: Observable<Any>
    }
}
