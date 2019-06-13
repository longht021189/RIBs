/*
 * Copyright (C) 2017. Uber Technologies
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.uber.rib.core;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import dagger.Lazy;

import javax.inject.Inject;

/**
 * The base implementation for all {@link Interactor}s.
 *
 * @param <P> the type of {@link Presenter}.
 * @param <R> the type of {@link Router}.
 */
public abstract class Interactor<P, R extends Router> extends InteractorBase<P, R> {

    private R router;
    @Inject P presenter;

    public Interactor() {
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
        Interactor<?, ?> interactor;

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
