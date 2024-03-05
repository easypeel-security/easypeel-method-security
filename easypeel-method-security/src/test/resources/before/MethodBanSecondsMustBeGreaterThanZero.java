package com.example;

import org.easypeelsecurity.core.MethodBan;

class HelloWorld {

  @MethodBan(times = 2, seconds = 0, banSeconds = 20)
  public String hello() {
    return "Hello World!";
  }
}