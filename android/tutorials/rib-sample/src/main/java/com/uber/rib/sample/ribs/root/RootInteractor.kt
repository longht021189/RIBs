package com.uber.rib.sample.ribs.root

import com.uber.autodispose.AutoDispose
import com.uber.rib.core.Bundle
import com.uber.rib.core.Interactor
import com.uber.rib.sample.NavigationManager
import dagger.Lazy
import javax.inject.Inject

@RootBuilder.RootScope
class RootInteractor @Inject constructor(
    presenter: Lazy<RootPresenter>,
    router: Lazy<RootRouter>,
    private val manager: NavigationManager
) : Interactor<RootInteractor.RootPresenter, RootRouter>(presenter, router) {

    override fun didBecomeActive(savedInstanceState: Bundle?) {
        super.didBecomeActive(savedInstanceState)

        manager.getNavigation(router.tag)
            ?.notify?.`as`(AutoDispose.autoDisposable(this))
            ?.subscribe {

            }
    }

    interface RootPresenter
}
