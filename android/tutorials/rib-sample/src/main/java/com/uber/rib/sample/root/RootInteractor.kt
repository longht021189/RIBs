package com.uber.rib.sample.root

import com.uber.rib.sample.Navigation

@RootBuilder.RootScope
class RootInteractor @javax.inject.Inject constructor(
    presenter: dagger.Lazy<RootPresenter>,
    router: dagger.Lazy<RootRouter>,
    private val navigation: com.uber.rib.core.navigation.Navigation
) : com.uber.rib.core.Interactor<RootInteractor.RootPresenter, RootRouter>(presenter, router),
    com.uber.rib.core.navigation.Node {

    private val nodeName = Navigation.NODE_ROOT
    private val backStackName = Navigation.BACK_STACK_MAIN
    private val nodeManager by lazy {
        navigation.getNodeManager(backStackName)
    }

    override fun didBecomeActive(savedInstanceState: com.uber.rib.core.Bundle?) {
        super.didBecomeActive(savedInstanceState)
        nodeManager.addNode(nodeName, this)
    }

    override fun onNavigation(child: String?) {
        when (child) {
            Navigation.NODE_HOME -> router.routeToHome()
            else -> router.routeToSplash()
        }
    }

    override fun willResignActive() {
        nodeManager.removeNode(nodeName)
        super.willResignActive()
    }

    interface RootPresenter
}
