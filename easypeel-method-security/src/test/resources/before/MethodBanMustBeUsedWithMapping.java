package com.example;

import org.easypeelsecurity.core.MethodBan;

class HelloWorld {

  @MethodBan(times = 3, seconds = 10, banSeconds = 20)
  public String hello() {
    return "Hello World!";
  }
}