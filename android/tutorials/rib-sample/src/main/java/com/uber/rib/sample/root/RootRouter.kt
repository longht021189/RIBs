package com.uber.rib.sample.root

import com.uber.rib.core.IViewRouter
import com.uber.rib.core.Router
import com.uber.rib.sample.home.HomeBuilder
import com.uber.rib.sample.splash.SplashBuilder

@RootBuilder.RootScope
class RootRouter @javax.inject.Inject constructor(
    view: RootView,
    interactor: RootInteractor,
    val injector: dagger.android.DispatchingAndroidInjector<Any>
) : com.uber.rib.core.ViewRouterSubcomponent<RootView, RootInteractor>(view, interactor) {

    private var router: Router<*>? = null

    fun routeToSplash() {
        this.router?.let {
            detachChild(it)

            if (it is IViewRouter<*>) {
                view.removeView(it.view)
            }
        }

        val router = SplashBuilder().build(injector)

        attachChild(router)
        view.addView(router.view)

        this.router = router
    }

    fun routeToHome() {
        this.router?.let {
            detachChild(it)

            if (it is IViewRouter<*>) {
                view.removeView(it.view)
            }
        }

        val router = HomeBuilder().build(injector)

        attachChild(router)
        view.addView(router.view)

        this.router = router
    }
}
