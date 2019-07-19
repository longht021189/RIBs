package com.uber.rib.sample.root

import android.view.ViewGroup
import com.uber.rib.core.IViewRouter
import com.uber.rib.core.Router
import com.uber.rib.core.ViewRouterSubcomponent
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Provider

@RootBuilder.RootScope
class RootRouter @Inject constructor(
    @RootBuilder.RootQualifier
    view: ViewGroup,
    interactor: RootInteractor,
    private val routersLazy: Lazy<Map<String, @JvmSuppressWildcards Provider<Router<*>>>>
) : ViewRouterSubcomponent<ViewGroup, RootInteractor>(view, interactor) {

    private var currentName: String? = null
    private var currentRouter: Router<*>? = null

    fun routeTo(child: String?) {
        if (child == currentName) return

        currentRouter?.let {
            detachChild(it)

            if (it is IViewRouter<*>) {
                view.removeView(it.view)
            }
        }
        currentRouter = null
        currentName = child

        val router = routersLazy.get()[currentName]?.get()
        if (router != null) {
            attachChild(router)

            if (router is IViewRouter<*>) {
                view.addView(router.view)
            }
        }

        currentRouter = router
    }
}
