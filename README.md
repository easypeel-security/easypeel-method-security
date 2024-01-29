# <img src="https://github.com/easypeel-security/spring-method-ban/assets/13290706/0d83c171-4f62-44b3-8a36-e3a86898b954" align="right" width="100">Easypeel Method Security

[![Maven Central](https://img.shields.io/maven-central/v/io.github.easypeel-security/easypeel-method-security.svg?label=Maven%20Central&color=)](https://mvnrepository.com/artifact/io.github.easypeel-security/easypeel-method-security)
[![test](https://github.com/easypeel-security/easypeel-method-security/actions/workflows/on-push.yml/badge.svg)](https://github.com/easypeel-security/easypeel-method-security/actions/workflows/on-push.yml)
[![codecov](https://codecov.io/gh/easypeel-security/easypeel-method-security/graph/badge.svg?token=9FUJAWJB5W)](https://codecov.io/gh/easypeel-security/easypeel-method-security)

You can easily configure method-level security in your
RestController through annotations.

This includes the ability to implement an IP-based ban on
specific methods, which is activated when certain traffic thresholds are reached.

> Since it's still in beta, the software currently has limited features. If you're interested in
> following the project's progress, please press the ⭐ button to stay updated.

## Dependencies

For gradle,

```groovy
implementation 'io.github.easypeel-security:easypeel-method-security:0.0.1'
```

For maven,

```xml
<dependency>
  <groupId>io.github.easypeel-security</groupId>
  <artifactId>easypeel-method-security</artifactId>
  <version>0.0.1</version>
</dependency>
```

## Quick Start

### 1. MethodBan

```java
@GetMapping("/")
@MethodBan(times = 3, seconds = 10, banSeconds = 1000)
public String hello() {
  return "Hello World!";
}
```

> Once a user accesses an API `3 times` within `10 seconds`, they are prevented from accessing the
> same API for `1000 banSeconds`.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
More information can be found in the [CONTRIBUTING.md] file.

[CONTRIBUTING.md]: documentation/CONTRIBUTING.md

## License

This project is licensed under the terms of the [apache 2.0] license.

[apache 2.0]: LICENSE.txt

