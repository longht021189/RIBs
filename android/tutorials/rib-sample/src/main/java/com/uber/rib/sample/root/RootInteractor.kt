package com.uber.rib.sample.root

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

@RootBuilder.RootScope
class RootInteractor @javax.inject.Inject constructor(
    presenter: dagger.Lazy<RootPresenter>,
    router: dagger.Lazy<RootRouter>,
    private val navigation: com.uber.rib.core.navigation.Navigation
) : com.uber.rib.core.Interactor<RootInteractor.RootPresenter, RootRouter>(presenter, router),
    com.uber.rib.core.navigation.Node {

    private val nodeName = "ROOT"
    private val backStackName = "MAIN"
    private val nodeManager by lazy {
        navigation.getNodeManager(backStackName)
    }
    private val disposables by lazy {
        CompositeDisposable()
    }

    override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
        super.didBecomeActive(savedInstanceState)
        nodeManager.addNode(nodeName, this)

        router.routeToSplash()
        disposables.add(presenter.click.subscribe {
            router.routeToHome()
        })
    }

    override fun onNavigation(pathSegments: List<String>) {

    }

    override fun willResignActive() {
        disposables.clear()
        nodeManager.removeNode(nodeName)

        super.willResignActive()
    }

    interface RootPresenter {
        val click: Observable<Any>
    }
}
