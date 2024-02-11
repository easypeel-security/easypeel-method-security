# <img src="https://github.com/easypeel-security/spring-method-ban/assets/13290706/0d83c171-4f62-44b3-8a36-e3a86898b954" align="right" width="100">Easypeel Method Security

[![Build Status](https://github.com/easypeel-security/easypeel-method-security/actions/workflows/on-push.yml/badge.svg)](https://github.com/easypeel-security/easypeel-method-security/actions/workflows/on-push.yml)
[![Coverage Status](https://codecov.io/gh/easypeel-security/easypeel-method-security/graph/badge.svg?token=9FUJAWJB5W)](https://codecov.io/gh/easypeel-security/easypeel-method-security)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.easypeel-security/easypeel-method-security.svg?label=Maven%20Central&color=)](https://mvnrepository.com/artifact/io.github.easypeel-security/easypeel-method-security)
[![JavaDoc](https://javadoc.io/badge2/io.github.easypeel-security/easypeel-method-security/javadoc.svg)](https://javadoc.io/doc/io.github.easypeel-security/easypeel-method-security)
[![License](https://img.shields.io/:license-apache-brightgreen.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

You can easily configure method-level security in your
RestController through annotations.

> Since it's still in beta, the software currently has limited features. If you're interested in
> following the project's progress, please press the ⭐ button to stay updated.

## Requirements

> ⚠️ **Requires Spring Boot 3.x**

## Dependencies

```groovy
annotationProcessor 'io.github.easypeel-security:easypeel-method-security:0.0.3'
```

```xml

<dependency>
  <groupId>io.github.easypeel-security</groupId>
  <artifactId>easypeel-method-security</artifactId>
  <version>0.0.3</version>
  <scope>provided</scope>
</dependency>
```

## Quick Start

### 1. MethodBan

The @MethodBan enables you to implement a simple Rate Limit within your controller.
If you need more details, please check the [details] page.

[details]: https://github.com/easypeel-security/easypeel-method-security/wiki/MethodBan

**1.1. Only IP based ban :**

```java

@GetMapping("/")
@MethodBan(times = 3, seconds = 10, banSeconds = 1000) // this
public String hello() {
  return "Hello World!";
}
```

> Once a `same IP` accesses an API `3 times` within `10 seconds`, they are prevented from accessing
> the same API for `1000 banSeconds`.

**1.2. IP & User based ban :**

```java

@PostMapping("/")
@MethodBan(times = 3, seconds = 10, banSeconds = 1000,
    banMessage = "You're writing too fast. Please try again later.",
    additionalFilter = @ParameterFilter(name = "enterpriseUser")) // this
public void createJobPosting(
    @CurrentUser EnterpriseUserAccount enterpriseUser) {
  // ... 

}
```

> Once a `same IP and User Credential` accesses an API `3 times` within `10 seconds`, they are
> prevented from accessing the same API for `1000 banSeconds`.

### 2. Another Feature is Coming Soon!

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
More information can be found in the [CONTRIBUTING.md] file.

[CONTRIBUTING.md]: documentation/CONTRIBUTING.md

## License

This project is licensed under the terms of the [apache 2.0] license.

[apache 2.0]: LICENSE.txt

