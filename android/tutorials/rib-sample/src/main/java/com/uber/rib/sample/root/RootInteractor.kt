package com.uber.rib.sample.root

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.navigation.Navigation
import com.uber.rib.sample.NavigationUtil
import dagger.Lazy
import javax.inject.Inject

@RootBuilder.RootScope
class RootInteractor @Inject constructor(
    presenter: Lazy<RootPresenter>,
    router: Lazy<RootRouter>,
    private val navigation: Lazy<Navigation>
) : Interactor<RootInteractor.RootPresenter, RootRouter>(presenter, router) {

    private val backStackName: String by lazy {
        NavigationUtil.BACK_STACK_MAIN
    }
    private val nodeManager by lazy {
        navigation.get().getNodeManager(backStackName)
    }

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)
        nodeManager.addNode(NAME, this)
    }

    override fun onNavigation(child: String?, data: Any?, queryParameters: Map<String, String?>?) {
        router.routeTo(child)
    }

    override fun willResignActive() {
        nodeManager.removeNode(NAME)
        super.willResignActive()
    }

    interface RootPresenter

    companion object {
        const val NAME = "ROOT"
    }
}
