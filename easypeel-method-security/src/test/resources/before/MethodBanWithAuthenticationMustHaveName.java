package com.example;

import org.easypeelsecurity.core.MethodBan;
import org.easypeelsecurity.core.ParameterFilter;

class HelloWorld {

  @MethodBan(additionalFilter = @ParameterFilter(isEnabled = true))
  public String hello() {
    return "Hello World!";
  }
}