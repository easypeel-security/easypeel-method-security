package com.example;

import org.epsec.core.MethodBan;

class HelloWorld {

  @MethodBan(times = 2, seconds = 0, banSeconds = 20)
  public String hello() {
    return "Hello World!";
  }
}