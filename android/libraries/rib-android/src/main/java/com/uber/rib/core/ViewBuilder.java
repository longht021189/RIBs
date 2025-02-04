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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Router builder for routers that own a view.
 *
 * @param <ViewType> type of view owned by router.
 * @param <RouterT> type of router built by this builder.
 * @param <DependencyT> dependency required to create this router.
 */
@Deprecated
public abstract class ViewBuilder<ViewType extends View, RouterT extends Router, DependencyT>
    extends Builder<RouterT, DependencyT> {

  public ViewBuilder(DependencyT dependency) {
    super(dependency);
  }

  /**
   * Utility method to create the view for this router.
   *
   * @param parentViewGroup to inflate view with.
   * @return the view for a new router.
   */
  protected final ViewType createView(ViewGroup parentViewGroup) {
    final Context context = parentViewGroup.getContext();
    final ViewType view =
        inflateView(LayoutInflater.from(onThemeContext(context)), parentViewGroup);

    if (XRay.isEnabled()) {
      XRay.apply(this, view);
    }

    return view;
  }

  /**
   * Inflates the router's view with knowledge of its parent. This should never be called directly,
   * instead use {@link ViewBuilder#createView(ViewGroup)} which will automatically pass the correct
   * context.
   *
   * @param inflater to inflate view with.
   * @param parentViewGroup to use for layout parameters.
   * @return the new view, not attached to its parent.
   */
  protected abstract ViewType inflateView(LayoutInflater inflater, ViewGroup parentViewGroup);

  /**
   * Optional override that allows the implementation to hook into the context to theme it with a
   * potentially different one before view creation, such as with {@link
   * android.view.ContextThemeWrapper}.
   *
   * @param parentContext the original parent context, and default used if this method isn't
   *     overridden.
   * @return the possibly themed context.
   */
  protected Context onThemeContext(Context parentContext) {
    return parentContext;
  }
}
