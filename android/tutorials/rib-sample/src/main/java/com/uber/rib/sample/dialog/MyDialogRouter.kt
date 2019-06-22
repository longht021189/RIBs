package com.uber.rib.sample.dialog

@MyDialogBuilder.MyDialogScope
class MyDialogRouter @javax.inject.Inject constructor(
    view: MyDialogView,
    interactor: MyDialogInteractor,
    val injector: dagger.android.DispatchingAndroidInjector<Any>
) : com.uber.rib.core.ViewRouterSubcomponent<MyDialogView, MyDialogInteractor>(view, interactor)
