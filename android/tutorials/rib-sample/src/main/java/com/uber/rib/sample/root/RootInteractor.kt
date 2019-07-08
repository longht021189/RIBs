package com.uber.rib.sample.root

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.navigation.Navigation
import com.uber.rib.core.navigation.Node
import dagger.Lazy
import javax.inject.Inject

@RootBuilder.RootScope
class RootInteractor @Inject constructor(
    presenter: Lazy<RootPresenter>,
    router: Lazy<RootRouter>,
    private val navigation: Lazy<Navigation>
) : Interactor<RootInteractor.RootPresenter, RootRouter>(presenter, router) {

    private val nodeName: String = "ROOT"
    private val backStackName: String = TODO("backStackName, String HERE")
    private val nodeManager by lazy {
        navigation.get().getNodeManager(backStackName)
    }

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        nodeManager.addNode(nodeName, this)

        // TODO: Add attachment logic here (RxSubscriptions, etc.).
    }

    override fun willResignActive() {
        // TODO: Perform any required clean up here, or delete this method entirely if not needed.

        nodeManager.removeNode(nodeName)
        super.willResignActive()
    }

    interface RootPresenter
}
