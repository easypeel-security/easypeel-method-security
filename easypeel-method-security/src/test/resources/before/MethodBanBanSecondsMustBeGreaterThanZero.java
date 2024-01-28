package com.example;

import org.epsec.core.MethodBan;

class HelloWorld {

  @MethodBan(times = 2, seconds = 1, banSeconds = 0)
  public String hello() {
    return "Hello World!";
  }
}