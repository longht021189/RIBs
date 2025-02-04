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
package com.uber.presidio.intellij_plugin.generator.rib;

import com.uber.presidio.intellij_plugin.generator.Generator;

public class ViewPresenterGenerator extends Generator {

  private static final String TEMPLATE_NAME = "RibPresenter";

  public ViewPresenterGenerator(String packageName, String ribName, boolean isKotlinSelected,
                                boolean isSubcomponent, boolean useQualifierView, boolean useQualifierViewGroup, boolean createViewAsync) {
    super(packageName, ribName, isKotlinSelected, TEMPLATE_NAME, isSubcomponent,
            false, useQualifierView, useQualifierViewGroup, createViewAsync);
  }

  @Override
  public String getClassName() {
    return String.format("%sPresenter", getRibName());
  }
}
