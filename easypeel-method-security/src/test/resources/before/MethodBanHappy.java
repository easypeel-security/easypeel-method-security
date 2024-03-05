package com.example;

import org.easypeelsecurity.core.MethodBan;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

class HelloWorld {

  @GetMapping("/get")
  @MethodBan(times = 3, seconds = 10, banSeconds = 20)
  public String get() {
    return "Hello World!";
  }

  @PostMapping("/post")
  @MethodBan(times = 3, seconds = 10, banSeconds = 20)
  public String post() {
    return "Hello World!";
  }
}