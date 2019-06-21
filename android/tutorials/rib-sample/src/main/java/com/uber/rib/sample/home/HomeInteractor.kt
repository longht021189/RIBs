package com.uber.rib.sample.home

@HomeBuilder.HomeScope
class HomeInteractor @javax.inject.Inject constructor(
    presenter: dagger.Lazy<HomePresenter>,
    router: dagger.Lazy<HomeRouter>,
    navigation: com.uber.rib.core.navigation.Navigation
) : com.uber.rib.core.Interactor<HomeInteractor.HomePresenter, HomeRouter>(presenter, router),
    com.uber.rib.core.navigation.Node {

    private val nodeName = "HOME"
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

    interface HomePresenter
}
