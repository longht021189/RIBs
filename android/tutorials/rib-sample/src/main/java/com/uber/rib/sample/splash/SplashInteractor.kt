package com.uber.rib.sample.splash

@SplashBuilder.SplashScope
class SplashInteractor @javax.inject.Inject constructor(
    presenter: dagger.Lazy<SplashPresenter>,
    router: dagger.Lazy<SplashRouter>,
    private val navigation: com.uber.rib.core.navigation.Navigation
) : com.uber.rib.core.Interactor<SplashInteractor.SplashPresenter, SplashRouter>(presenter, router),
    com.uber.rib.core.navigation.Node {

    private val nodeName = "SPLASH"
    private val backStackName = "MAIN"
    private val nodeManager by lazy {
        navigation.getNodeManager(backStackName)
    }

    override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
        super.didBecomeActive(savedInstanceState)
        nodeManager.addNode(nodeName, this)
    }

    override fun onNavigation(pathSegments: List<String>) {

    }

    override fun willResignActive() {
        nodeManager.removeNode(nodeName)
        super.willResignActive()
    }

    interface SplashPresenter
}
