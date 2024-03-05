package com.example;

import org.easypeelsecurity.core.MethodBan;
import org.easypeelsecurity.core.ParameterFilter;

class HelloWorld {

  @MethodBan(additionalFilter = @ParameterFilter(isEnabled = true, target = "adiwajdiajciawj"))
  public String hello(Object object) {
    return "Hello World!";
  }
}