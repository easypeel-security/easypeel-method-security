package com.example;

import org.easypeelsecurity.core.MethodBan;

class HelloWorld {

  @MethodBan(times = 1, seconds = 10, banSeconds = 20)
  private String hello() {
    return "Hello World!";
  }
}