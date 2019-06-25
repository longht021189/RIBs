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

public class ViewRouterGenerator extends Generator {

  private static final String TEMPLATE_NAME = "RibViewRouter";

  public ViewRouterGenerator(String packageName, String ribName, boolean isKotlinSelected, boolean isSubcomponent) {
    super(packageName, ribName, isKotlinSelected, TEMPLATE_NAME, isSubcomponent, false);
  }

  @Override
  public String getClassName() {
    return String.format("%sRouter", getRibName());
  }
}
