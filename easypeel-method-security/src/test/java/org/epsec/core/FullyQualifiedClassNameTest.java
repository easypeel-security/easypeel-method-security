/*
 * Copyright 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.epsec.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FullyQualifiedClassNameTest {

  @ParameterizedTest
  @CsvSource({
      "org.springframework.web.bind.annotation.GetMapping, GET_MAPPING",
      "org.springframework.web.bind.annotation.PostMapping, POST_MAPPING",
      "org.springframework.web.bind.annotation.PutMapping, PUT_MAPPING",
      "org.springframework.web.bind.annotation.DeleteMapping, DELETE_MAPPING",
      "org.springframework.web.bind.annotation.PatchMapping, PATCH_MAPPING",
      "org.springframework.stereotype.Component, COMPONENT",
      "org.springframework.context.annotation.EnableAspectJAutoProxy, ENABLE_ASPECT_JAUTO_PROXY",
      "org.aspectj.lang.JoinPoint, JOIN_POINT",
      "org.aspectj.lang.annotation.Aspect, ASPECT",
      "org.aspectj.lang.annotation.Before, BEFORE"
  })
  void getNameReturnWell(String expected, FullyQualifiedClassName fqcn) {
    assertEquals(expected, fqcn.getName());
  }

  @ParameterizedTest
  @CsvSource({
      "org.springframework.web.bind.annotation.GetMapping, true",
      "org.springframework.web.bind.annotation.PostMapping, true",
      "org.springframework.web.bind.annotation.PutMapping, true",
      "org.springframework.web.bind.annotation.DeleteMapping, true",
      "org.springframework.web.bind.annotation.PatchMapping, true",
      "org.springframework.stereotype.Component, false",
      "org.springframework.context.annotation.EnableAspectJAutoProxy, false",
      "org.aspectj.lang.JoinPoint, false",
      "org.aspectj.lang.annotation.Aspect, false",
      "org.aspectj.lang.annotation.Before, false"
  })
  void isWebAnnotation(String input, boolean expected) {
    assertEquals(expected, FullyQualifiedClassName.isWebAnnotation(input));
  }
}
