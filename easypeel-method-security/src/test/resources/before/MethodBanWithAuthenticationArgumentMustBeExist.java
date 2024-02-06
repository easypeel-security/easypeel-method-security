package com.example;

import org.epsec.core.MethodBan;
import org.epsec.core.ParameterFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloWorld {

  @GetMapping("/")
  @MethodBan(additionalFilter = @ParameterFilter(isEnabled = true, target = "object"))
  public String hello(Object object) {
    return "Hello World!";
  }
}