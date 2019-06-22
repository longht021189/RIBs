package com.uber.rib.sample.splash

import com.uber.rib.sample.Navigation
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

@SplashBuilder.SplashScope
class SplashInteractor @javax.inject.Inject constructor(
    presenter: dagger.Lazy<SplashPresenter>,
    router: dagger.Lazy<SplashRouter>,
    private val navigation: com.uber.rib.core.navigation.Navigation
) : com.uber.rib.core.Interactor<SplashInteractor.SplashPresenter, SplashRouter>(presenter, router),
    com.uber.rib.core.navigation.Node {

    private val nodeName = Navigation.NODE_SPLASH
    private val backStackName = Navigation.BACK_STACK_MAIN
    private val nodeManager by lazy {
        navigation.getNodeManager(backStackName)
    }
    private val disposables by lazy {
        CompositeDisposable()
    }

    override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
        super.didBecomeActive(savedInstanceState)
        nodeManager.addNode(nodeName, this)
        disposables.add(presenter.click.subscribe {
            nodeManager.add(Navigation.SCREEN_HOME)
        })
    }

    override fun onNavigation(child: String?) {

    }

    override fun willResignActive() {
        disposables.clear()
        nodeManager.removeNode(nodeName)
        super.willResignActive()
    }

    interface SplashPresenter {
        val click: Observable<Any>
    }
}
