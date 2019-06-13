package com.uber.rib.core;

import android.support.annotation.NonNull;
import dagger.Lazy;

import javax.inject.Inject;

public abstract class InteractorSubcomponent<P, R extends Router> extends InteractorBase<P, R> {

    @Inject
    public InteractorSubcomponent(@NonNull Lazy<P> presenter, @NonNull Lazy<R> router) {
        super(presenter, router);
    }
}
