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

/**
 * Responsible for handling the addition and removal of children routers.
 *
 * @param <I> type of interactor this router routes.
 * @param <C> type of dependency held by this router.
 */
public class RouterOld<I extends InteractorOld, C extends InteractorBaseComponent> extends Router<I> {
    @SuppressWarnings("unchecked")
    public RouterOld(C component, I interactor) {
        super(interactor);

        component.inject(interactor);
        interactor.setRouter(this);
    }

    @SuppressWarnings("unchecked")
    public RouterOld(C component, I interactor, RibRefWatcher ribRefWatcher, Thread mainThread) {
        super(interactor, ribRefWatcher, mainThread);

        component.inject(interactor);
        interactor.setRouter(this);
    }
}
