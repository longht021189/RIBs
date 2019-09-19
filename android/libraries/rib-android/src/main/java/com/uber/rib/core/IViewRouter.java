package com.uber.rib.core;

import android.view.View;
import androidx.annotation.NonNull;

public interface IViewRouter<V extends View> {
    V getView();
    void dispatchDetach();
    boolean handleBackPress();
    Interactor getInteractor();
    void saveInstanceState(Bundle bundle);
    void dispatchAttach(Bundle bundle);
    boolean onError(@NonNull Throwable error);
}
