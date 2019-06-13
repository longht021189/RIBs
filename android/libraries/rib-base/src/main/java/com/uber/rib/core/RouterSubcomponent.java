package com.uber.rib.core;

import javax.inject.Inject;

public class RouterSubcomponent<I extends Interactor> extends RouterBase<I> {

  @Inject
  public RouterSubcomponent(I interactor) {
    super(interactor);
  }
}
