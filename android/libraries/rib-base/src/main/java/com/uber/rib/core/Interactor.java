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

import android.content.Intent;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.Relay;
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction;
import com.uber.autodispose.lifecycle.LifecycleEndedException;
import com.uber.autodispose.lifecycle.LifecycleScopeProvider;
import com.uber.autodispose.lifecycle.LifecycleScopes;
import com.uber.rib.core.lifecycle.InteractorEvent;
import com.uber.rib.core.navigation.Node;

import java.util.Map;

import dagger.Lazy;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;

import static com.uber.rib.core.lifecycle.InteractorEvent.ACTIVE;
import static com.uber.rib.core.lifecycle.InteractorEvent.INACTIVE;

/**
 * The base implementation for all {@link Interactor}s.
 *
 * @param <P> the type of {@link Presenter}.
 * @param <R> the type of {@link Router}.
 */
public abstract class Interactor<P, R extends Router>
    implements LifecycleScopeProvider<InteractorEvent>, Node {

  private static final CorrespondingEventsFunction<InteractorEvent> LIFECYCLE_MAP_FUNCTION =
      interactorEvent -> {
        switch (interactorEvent) {
          case ACTIVE:
            return INACTIVE;
          default:
            throw new LifecycleEndedException();
        }
      };

  private final BehaviorRelay<InteractorEvent> behaviorRelay = BehaviorRelay.create();
  private final Relay<InteractorEvent> lifecycleRelay = behaviorRelay.toSerialized();

  private final Lazy<P> lazyPresenter;
  private final Lazy<R> lazyRouter;

  public Interactor(@NonNull Lazy<P> presenter, @NonNull Lazy<R> router) {
    this.lazyPresenter = presenter;
    this.lazyRouter = router;

    postInit(presenter, router);
  }

  protected void postInit(@NonNull Lazy<P> presenter, @NonNull Lazy<R> router) {}

  /** @return the router for this interactor. */
  public R getRouter() {
    R router = lazyRouter.get();
    if (router == null) {
      throw new IllegalStateException("Attempting to get interactor's router before being set.");
    }
    return router;
  }

  /** @return an observable of this controller's lifecycle events. */
  @Override
  public Observable<InteractorEvent> lifecycle() {
    return lifecycleRelay.hide();
  }

  /** @return true if the controller is attached, false if not. */
  public boolean isAttached() {
    return behaviorRelay.getValue() == ACTIVE;
  }

  /**
   * Called when attached. The presenter will automatically be added when this happens.
   *
   * @param savedInstanceState the saved {@link Bundle}.
   */
  @CallSuper
  protected void didBecomeActive(@Nullable Bundle savedInstanceState) {}

  /**
   * Handle an activity back press.
   *
   * @return whether this interactor took action in response to a back press.
   */
  @Override
  public boolean handleBackPress() {
    return false;
  }

  /**
   * Called when detached. The {@link Interactor} should do its cleanup here. Note: View will be
   * removed automatically so {@link Interactor} doesn't have to remove its view here.
   */
  protected void willResignActive() {}

  /**
   * Called when saving state.
   *
   * @param outState the saved {@link Bundle}.
   */
  protected void onSaveInstanceState(Bundle outState) {}

  protected void dispatchAttach(@Nullable Bundle savedInstanceState) {
    lifecycleRelay.accept(ACTIVE);

    if (getPresenter() instanceof Presenter) {
      // Legacy support for lifecycled presenters.
      Presenter presenter = (Presenter) getPresenter();
      presenter.dispatchLoad();
    }
    didBecomeActive(savedInstanceState);
  }

  protected P dispatchDetach() {
    if (getPresenter() instanceof Presenter) {
      // Legacy support for lifecycled presenters.
      Presenter presenter = (Presenter) getPresenter();
      presenter.dispatchUnload();
    }
    willResignActive();

    lifecycleRelay.accept(INACTIVE);
    return getPresenter();
  }

  /** @return the currently attached presenter if there is one */
  @NonNull
  public P getPresenter() {
    P presenter = lazyPresenter.get();
    if (presenter == null) {
      throw new IllegalStateException("Attempting to get interactor's presenter before being set.");
    }
    return presenter;
  }

  @Override
  public CorrespondingEventsFunction<InteractorEvent> correspondingEvents() {
    return LIFECYCLE_MAP_FUNCTION;
  }

  @Override
  public InteractorEvent peekLifecycle() {
    return behaviorRelay.getValue();
  }

  @Override
  public final CompletableSource requestScope() {
    return LifecycleScopes.resolveScopeFromLifecycle(this);
  }

  @Override
  public void onNavigation(@Nullable String child, @Nullable Object data,
                           @Nullable Map<String, String> queryParameters) { }

  @Nullable
  @Override
  public Object onEnterBackStack(@Nullable Object data) {
    return data;
  }

  public boolean onError(@NonNull Object error) {
    return false;
  }

  public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
    return false;
  }

  public boolean onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    return false;
  }
}
