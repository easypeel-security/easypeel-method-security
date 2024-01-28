# <img src="https://github.com/easypeel-security/spring-method-ban/assets/13290706/0d83c171-4f62-44b3-8a36-e3a86898b954" align="right" width="100">Easypeel Method Security

You can easily configure method-level security in your
RestController through annotations.

This includes the ability to implement an IP-based ban on
specific methods, which is activated when certain traffic thresholds are reached.

## Dependencies

gradle
```yml
    implementation 'org.epsec:easypeel-method-security:0.0.1'
```

maven
```xml
    <dependency>
      <groupId>org.epsec</groupId>
      <artifactId>easypeel-method-security</artifactId>
      <version>0.0.1</version>
    </dependency>
```

## Quick Start

1. MethodBan

    ```java
    @GetMapping("/")
    @MethodBan(times = 3, seconds = 10, banSeconds = 1000)
    public String hello() {
      return "Hello World!";
    }
    ```
   
    > User can only access the method 3 times in 10 second, and if the user exceeds the threshold, the user will be banned for 1000 second to same api.

