package com.uber.rib.core;

import android.view.View;
import io.reactivex.Observable;

public interface IViewRouter<V extends View> {
    V getView();
    Observable<Object> dispatchDetach();
    boolean handleBackPress();
    Interactor getInteractor();
    void saveInstanceState(Bundle bundle);
    void dispatchAttach(Bundle bundle);
}
