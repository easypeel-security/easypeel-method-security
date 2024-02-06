package com.example;

import org.epsec.core.MethodBan;
import org.epsec.core.ParameterFilter;

class HelloWorld {

  @MethodBan(additionalFilter = @ParameterFilter(isEnabled = true, target = "adiwajdiajciawj"))
  public String hello(Object object) {
    return "Hello World!";
  }
}