package com.uber.rib.core;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import dagger.Lazy;

import javax.inject.Inject;

public abstract class InteractorOld<P, R extends RouterOld> extends Interactor<P, R> {

    private R router;
    @Inject P presenter;

    public InteractorOld() {
        super(new LazyObject<>(Type.PRESENTER), new LazyObject<>(Type.ROUTER));
    }

    @Override
    protected void postInit(@NonNull Lazy<P> presenter, @NonNull Lazy<R> router) {
        ((LazyObject<P>) presenter).interactor = this;
        ((LazyObject<R>) router).interactor = this;
    }

    protected void setRouter(R router) {
        if (this.router == null) {
            this.router = router;
        } else {
            throw new IllegalStateException(
                    "Attempting to set interactor's router after it has been set.");
        }
    }

    @Nullable
    private <T> T getData(@NonNull Type type) {
        switch (type) {
            case ROUTER:
                return (T) router;

            case PRESENTER:
                return (T) presenter;
        }

        return null;
    }

    private static class LazyObject<T> implements Lazy<T> {
        private final Type type;
        InteractorOld<?, ?> interactor;

        LazyObject(Type type) {
            this.type = type;
        }

        @Override
        public T get() {
            if (interactor == null) {
                return null;
            }

            return interactor.getData(type);
        }
    }

    private enum Type {
        PRESENTER,
        ROUTER
    }
}
