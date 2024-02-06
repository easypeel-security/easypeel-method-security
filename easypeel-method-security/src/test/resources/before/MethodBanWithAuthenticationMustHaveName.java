package com.example;

import org.epsec.core.MethodBan;
import org.epsec.core.ParameterFilter;

class HelloWorld {

  @MethodBan(additionalFilter = @ParameterFilter(isEnabled = true))
  public String hello() {
    return "Hello World!";
  }
}