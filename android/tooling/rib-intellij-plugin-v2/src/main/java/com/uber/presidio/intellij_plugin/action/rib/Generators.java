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
package com.uber.presidio.intellij_plugin.action.rib;

import com.google.common.collect.ImmutableList;
import com.uber.presidio.intellij_plugin.generator.GeneratorPair;
import com.uber.presidio.intellij_plugin.generator.rib.*;

/**
 * Utility methods for getting lists of generators in different configurations.
 */
public final class Generators {

  private Generators() {
  }

  /**
   * @param packageName to use for generators.
   * @param ribName     to use for generators.
   * @return a list of generators to use when generating a rib with a presenter and view.
   */
  public static GeneratorPair getGeneratorsForRibWithPresenterAndView(
      String packageName, String ribName, boolean isKotlinSelected, boolean isSubcomponent,
      boolean createLayout, boolean createViewAsync, boolean useNavigation,
      boolean useQualifierView, boolean useQualifierViewGroup) {

    if (isSubcomponent && !isKotlinSelected) {
      throw new RuntimeException("Java Subcomponent is not Supported yet.");
    }
    if (createLayout) {
      throw new RuntimeException("Create Layout is not Supported yet.");
    }

    InteractorWithPresenterGenerator interactorGenerator = new InteractorWithPresenterGenerator(packageName, ribName, isKotlinSelected, isSubcomponent, useNavigation, createViewAsync);
    ViewBuilderGenerator viewBuilderGenerator = new ViewBuilderGenerator(packageName, ribName, isKotlinSelected, isSubcomponent, useQualifierView, useQualifierViewGroup, createViewAsync);
    ViewGenerator viewGenerator = new ViewGenerator(packageName, ribName, isKotlinSelected, isSubcomponent, createViewAsync);
    ViewRouterGenerator viewRouterGenerator = new ViewRouterGenerator(packageName, ribName, isKotlinSelected, isSubcomponent, useQualifierView, useQualifierViewGroup, createViewAsync);

    // TODO Subcomponent Packet is not Support Test yet
    if (isSubcomponent) {
      if (createViewAsync) {
        ViewPresenterGenerator viewPresenterGenerator = new ViewPresenterGenerator(packageName,
                ribName, isKotlinSelected, isSubcomponent, useQualifierView, useQualifierViewGroup, createViewAsync);

        return new GeneratorPair(
            ImmutableList.of(
                interactorGenerator,
                viewBuilderGenerator,
                viewGenerator,
                viewRouterGenerator,
                viewPresenterGenerator),
            ImmutableList.of()
        );
      }

      return new GeneratorPair(
          ImmutableList.of(
              interactorGenerator,
              viewBuilderGenerator,
              viewGenerator,
              viewRouterGenerator),
          ImmutableList.of()
      );
    }

    InteractorWithPresenterTestGenerator interactorWithPresenterTestGenerator =
        new InteractorWithPresenterTestGenerator(packageName, ribName, isKotlinSelected, isSubcomponent);
    ViewRouterTestGenerator viewRouterTestGenerator =
        new ViewRouterTestGenerator(packageName, ribName, isKotlinSelected, isSubcomponent);

    return new GeneratorPair(
        ImmutableList.of(
            interactorGenerator,
            viewBuilderGenerator,
            viewGenerator,
            viewRouterGenerator),
        ImmutableList.of(interactorWithPresenterTestGenerator, viewRouterTestGenerator));
  }

  /**
   * @param packageName to use for generators.
   * @param ribName     to use for generators.
   * @return a list of generators to use when generating a rib without a presenter and view.
   */
  public static GeneratorPair getGeneratorsForRibWithoutPresenterAndView(
      String packageName, String ribName, boolean isKotlinSelected, boolean isSubcomponent, boolean useNavigation) {
    InteractorWithEmptyPresenterGenerator interactorGenerator =
        new InteractorWithEmptyPresenterGenerator(packageName, ribName, isKotlinSelected, isSubcomponent, useNavigation);
    BuilderGenerator builderGenerator = new BuilderGenerator(packageName, ribName, isKotlinSelected, isSubcomponent);
    RouterGenerator routerGenerator = new RouterGenerator(packageName, ribName, isKotlinSelected, isSubcomponent);

    // TODO Subcomponent Packet is not Support Test yet
    if (isSubcomponent) {
      return new GeneratorPair(
          ImmutableList.of(interactorGenerator, builderGenerator, routerGenerator),
          ImmutableList.of()
      );
    }

    InteractorWithEmptyPresenterTestGenerator interactorWithEmptyPresenterTestGenerator =
        new InteractorWithEmptyPresenterTestGenerator(packageName, ribName, isKotlinSelected, isSubcomponent);
    RouterTestGenerator routerTestGenerator = new RouterTestGenerator(packageName, ribName, isKotlinSelected, isSubcomponent);

    return new GeneratorPair(
        ImmutableList.of(interactorGenerator, builderGenerator, routerGenerator),
        ImmutableList.of(interactorWithEmptyPresenterTestGenerator, routerTestGenerator));
  }
}
