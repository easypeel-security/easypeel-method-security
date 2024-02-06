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

package org.epsec.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("all")
class StringUtilsTest {

  @ParameterizedTest
  @CsvSource({
      ", false",
      "'', false",
      "' ', false",
      "'  ', false",
      "'a', true",
      "' a', true",
      "'a ', true",
      "' a ', true",
      "'a b', true",
      "' a b', true",
      "'a b ', true",
      "' a b ', true",
  })
  void hasTextTest(String str, boolean expected) {
    // when
    boolean result = StringUtils.hasText(str);

    // then
    assertEquals(expected, result);
  }
}
