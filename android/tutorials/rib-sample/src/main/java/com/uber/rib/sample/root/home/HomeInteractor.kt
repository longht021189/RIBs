package com.uber.rib.sample.root.home

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.navigation.Navigation
import com.uber.rib.sample.NavigationUtil
import dagger.Lazy
import javax.inject.Inject

@HomeBuilder.HomeScope
class HomeInteractor @Inject constructor(
    presenter: Lazy<HomePresenter>,
    router: Lazy<HomeRouter>,
    private val navigation: Lazy<Navigation>
) : Interactor<HomeInteractor.HomePresenter, HomeRouter>(presenter, router) {

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

    override fun willResignActive() {
        nodeManager.removeNode(NAME)
        super.willResignActive()
    }

    interface HomePresenter

    companion object {
        const val NAME = "HOME"
    }
}
