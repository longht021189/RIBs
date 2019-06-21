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

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction;
import com.uber.autodispose.lifecycle.LifecycleEndedException;
import com.uber.autodispose.lifecycle.LifecycleScopeProvider;
import com.uber.autodispose.lifecycle.LifecycleScopes;
import com.uber.rib.core.lifecycle.ActivityCallbackEvent;
import com.uber.rib.core.lifecycle.ActivityLifecycleEvent;
import io.reactivex.CompletableSource;
import io.reactivex.Observable;
import io.reactivex.functions.Predicate;

/** Base implementation for all VIP {@link android.app.Activity}s. */
public abstract class RibDialogFragment extends DialogFragment
    implements ActivityStarter, LifecycleScopeProvider<ActivityLifecycleEvent>, RxActivityEvents {

  /**
   * Figures out which corresponding next lifecycle event in which to unsubscribe, for Activities.
   */
  private static final CorrespondingEventsFunction<ActivityLifecycleEvent> ACTIVITY_LIFECYCLE =
      lastEvent -> {
        switch (lastEvent.getType()) {
          case CREATE:
            return ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.DESTROY);
          case START:
            return ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.STOP);
          case RESUME:
            return ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.PAUSE);
          case PAUSE:
            return ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.STOP);
          case STOP:
            return ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.DESTROY);
          case DESTROY:
            throw new LifecycleEndedException(
                "Cannot bind to Activity lifecycle when outside of it.");
        }
        throw new UnsupportedOperationException("Binding to " + lastEvent + " not yet implemented");
      };

  @SuppressWarnings("NullableProblems")
  private ViewRouter<?, ?, ?> router;

  private final BehaviorRelay<ActivityLifecycleEvent> lifecycleBehaviorRelay =
      BehaviorRelay.create();
  private final Relay<ActivityLifecycleEvent> lifecycleRelay =
      lifecycleBehaviorRelay.toSerialized();
  private final Relay<ActivityCallbackEvent> callbacksRelay =
      PublishRelay.<ActivityCallbackEvent>create().toSerialized();

  /** @return an observable of this activity's lifecycle events. */
  @Override
  public Observable<ActivityLifecycleEvent> lifecycle() {
    return lifecycleRelay.hide();
  }

  /**
   * @param <T> The type of {@link ActivityLifecycleEvent} subclass you want.
   * @param clazz The {@link ActivityLifecycleEvent} subclass you want.
   * @return an observable of this activity's lifecycle events.
   */
  public <T extends ActivityLifecycleEvent> Observable<T> lifecycle(final Class<T> clazz) {
    return lifecycle()
        .filter(
            new Predicate<ActivityLifecycleEvent>() {
              @Override
              public boolean test(ActivityLifecycleEvent activityEvent) throws Exception {
                return clazz.isAssignableFrom(activityEvent.getClass());
              }
            })
        .cast(clazz);
  }

  /** @return an observable of this activity's lifecycle events. */
  @Override
  public Observable<ActivityCallbackEvent> callbacks() {
    return callbacksRelay.hide();
  }

  /**
   * @param <T> The type of {@link ActivityCallbackEvent} subclass you want.
   * @param clazz The {@link ActivityCallbackEvent} subclass you want.
   * @return an observable of this activity's callbacks events.
   */
  public <T extends ActivityCallbackEvent> Observable<T> callbacks(final Class<T> clazz) {
    return callbacks()
        .filter(
            new Predicate<ActivityCallbackEvent>() {
              @Override
              public boolean test(ActivityCallbackEvent activityCallbackEvent) throws Exception {
                return clazz.isAssignableFrom(activityCallbackEvent.getClass());
              }
            })
        .cast(clazz);
  }

  @Override
  public CorrespondingEventsFunction<ActivityLifecycleEvent> correspondingEvents() {
    return ACTIVITY_LIFECYCLE;
  }

  @Nullable
  @Override
  public ActivityLifecycleEvent peekLifecycle() {
    return lifecycleBehaviorRelay.getValue();
  }

  @Override
  public CompletableSource requestScope() {
    return LifecycleScopes.resolveScopeFromLifecycle(this);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(android.os.Bundle savedInstanceState) {
    return new RibDialog(getActivity(), getTheme(), this);
  }

  public void onCreateDialog(Dialog dialog) {}

  @Nullable
  @Override
  @CallSuper
  @Initializer
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable android.os.Bundle savedInstanceState) {
    lifecycleRelay.accept(ActivityLifecycleEvent.createOnCreateEvent(savedInstanceState));
    router = createRouter(container);

    Bundle wrappedBundle = null;
    if (savedInstanceState != null) {
      wrappedBundle = new Bundle(savedInstanceState);
    }
    router.dispatchAttach(wrappedBundle);

    return router.getView();
  }

  @Override
  @CallSuper
  public void onSaveInstanceState(android.os.Bundle outState) {
    super.onSaveInstanceState(outState);
    callbacksRelay.accept(ActivityCallbackEvent.createOnSaveInstanceStateEvent(outState));
    Preconditions.checkNotNull(router).saveInstanceState(new Bundle(outState));
  }

  @Override
  @CallSuper
  public void onStart() {
    super.onStart();
    lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.START));
  }

  @Override
  @CallSuper
  public void onResume() {
    super.onResume();

    lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.RESUME));
  }

  @Override
  @CallSuper
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    callbacksRelay.accept(
        ActivityCallbackEvent.createOnActivityResultEvent(requestCode, resultCode, data));
  }

  @Override
  @CallSuper
  public void onPause() {

    lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.PAUSE));
    super.onPause();
  }

  @Override
  @CallSuper
  public void onStop() {
    lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.STOP));
    super.onStop();
  }

  @Override
  @CallSuper
  @SuppressWarnings("CheckNullabilityTypes")
  public void onDestroyView() {
    if (lifecycleRelay != null) {
      lifecycleRelay.accept(ActivityLifecycleEvent.create(ActivityLifecycleEvent.Type.DESTROY));
    }
    if (router != null) {
      router.dispatchDetach();
    }
    router = null;
    super.onDestroyView();
  }

  @Override
  @CallSuper
  public void onLowMemory() {
    super.onLowMemory();
    callbacksRelay.accept(ActivityCallbackEvent.create(ActivityCallbackEvent.Type.LOW_MEMORY));
  }

  @Override
  public void dismiss() {
    if (router != null && !router.handleBackPress()) {
      super.dismiss();
    }
  }

  /**
   * @return the {@link Interactor} when the activity has alive.
   * throws IllegalStateException if the activity has not been created or has been destroyed.
   */
  protected Interactor getInteractor() {
    if (router != null) {
      return router.getInteractor();
    } else {
      throw new IllegalStateException(
          "Attempting to get a router when activity is not created or has been " + "destroyed.");
    }
  }

  /**
   * Creates the {@link Interactor}.
   *
   * @return the {@link Interactor}.
   */
  protected abstract ViewRouter<?, ?, ?> createRouter(ViewGroup parentViewGroup);

  public static class RibDialog extends Dialog {
    private final RibDialogFragment fragment;

    public RibDialog(@NonNull Context context, int themeResId, RibDialogFragment fragment) {
      super(context, themeResId);
      this.fragment = fragment;
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      fragment.onCreateDialog(this);
    }
  }
}
