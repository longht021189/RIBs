package com.uber.rib.sample.root.splash

import com.uber.rib.core.Bundle
import com.uber.rib.core.NavigationInteractor
import com.uber.rib.core.navigation.Navigation
import com.uber.rib.sample.NavigationUtil
import dagger.Lazy
import javax.inject.Inject

@SplashBuilder.SplashScope
class SplashInteractor @Inject constructor(
    presenter: Lazy<SplashPresenter>,
    router: Lazy<SplashRouter>,
    navigation: Lazy<Navigation>
) : NavigationInteractor<SplashInteractor.SplashPresenter, SplashRouter>(
    presenter, router, navigation, NavigationUtil.BACK_STACK_MAIN, NAME
) {
    override fun didBecomeActive(savedInstanceState: Bundle?, child: String?, data: Any?) {

    }

    interface SplashPresenter

    companion object {
        const val NAME = "SPLASH"
    }
}
