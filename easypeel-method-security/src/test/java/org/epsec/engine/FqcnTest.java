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

package org.epsec.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FqcnTest {

  @ParameterizedTest
  @CsvSource({
      "org.epsec.engine, method1, org.epsec.engine.method1",
      "org.epsec.engine, method2, org.epsec.engine.method2",
      "org.springframework.security, hello, org.springframework.security.hello"})
  void fqcnToStringHappy(String packageName, String methodName, String expected) {
    // given
    final Fqcn fqcn = new Fqcn(packageName, methodName);

    // when
    final String actual = fqcn.toString();

    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({
      "org.epsec.engine,",
      ", method2"})
  void fqcnToStringSad(String packageName, String methodName) {
    // when & then
    assertThrows(IllegalArgumentException.class, () -> new Fqcn(packageName, methodName),
        "Cannot create Fqcn with empty string");
  }

  @ParameterizedTest
  @CsvSource({
      "org.epsec.engine, method1, org.epsec.engine, method1, true",
      "org.epsec.engine, method2, org.epsec.engine, method2, true",
      "org.springframework.security, hello, org.springframework.security, hello, true",
      "org.epsec.engine, method1, org.epsec.engine, method2, false",
      "org.epsec.engine, method2, org.epsec.engine, method1, false",
      "org.springframework.security, hello, org.springframework.security, hi, false",
  })
  void testEquals(String packageName1, String methodName1,
      String packageName2, String methodName2, boolean expected) {
    // given
    final Fqcn fqcn1 = new Fqcn(packageName1, methodName1);
    final Fqcn fqcn2 = new Fqcn(packageName2, methodName2);

    // when
    final boolean actual = fqcn1.equals(fqcn2);

    // then
    assertEquals(expected, actual);
  }

  @ParameterizedTest
  @CsvSource({
      "org.epsec.engine, method1",
      "org.epsec.engine, method2",
      "org.springframework.security, hello"})
  void testHashCode(String packageName, String methodName) {
    // given
    final Fqcn fqcn1 = new Fqcn(packageName, methodName);
    final Fqcn fqcn2 = new Fqcn(packageName, methodName);

    // when
    final int actual = fqcn1.hashCode();

    // then
    assertEquals(fqcn2.hashCode(), actual);
  }
}
