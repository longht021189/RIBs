package com.uber.rib.sample.root.splash

import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.core.navigation.Navigation
import com.uber.rib.sample.NavigationUtil
import dagger.Lazy
import javax.inject.Inject

@SplashBuilder.SplashScope
class SplashInteractor @Inject constructor(
    presenter: Lazy<SplashPresenter>,
    router: Lazy<SplashRouter>,
    private val navigation: Lazy<Navigation>
) : Interactor<SplashInteractor.SplashPresenter, SplashRouter>(presenter, router) {

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

    override fun onNavigation(child: String?, data: Any?) {
        super.onNavigation(child, data)
    }

    override fun onEnterBackStack(data: Any?): Any? {
        return (data as? Boolean)?.let { !it } ?: false
    }

    override fun willResignActive() {
        nodeManager.removeNode(NAME)
        super.willResignActive()
    }

    interface SplashPresenter

    companion object {
        const val NAME = "SPLASH"
    }
}
