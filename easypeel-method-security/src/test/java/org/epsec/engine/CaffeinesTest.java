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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CaffeinesTest {

  static final Fqcn FQCN = new Fqcn("org.epsec.engine", "testMethod");
  static final String IP = "Test";

  @Test
  void isOverAccessTimeReturnTrue() {
    // given
    final Caffeines caffeines = new Caffeines(FQCN);
    final int accessLimit = 3;
    caffeines.incrementVisit(IP);
    caffeines.incrementVisit(IP);
    caffeines.incrementVisit(IP);

    // when
    final boolean actual = caffeines.isOverAccessTime(IP, accessLimit);

    // then
    assertTrue(actual);
  }

  @Test
  void isOverAccessTimeDoesNotIncrementAccessCount() {
    // given
    final Caffeines caffeines = new Caffeines(FQCN);
    final int accessLimit = 1;
    // when & then
    assertFalse(caffeines.isOverAccessTime(IP, accessLimit));
    assertFalse(caffeines.isOverAccessTime(IP, accessLimit));
  }

  @Test
  void isOverAccessTimeReturnFalse() {
    // given
    final Caffeines caffeines = new Caffeines(FQCN);
    final int accessLimit = 3;
    caffeines.incrementVisit(IP);
    caffeines.incrementVisit(IP);

    // when
    final boolean actual = caffeines.isOverAccessTime(IP, accessLimit);

    // then
    assertFalse(actual);
  }

  @Test
  void cachesFieldIsSingleton() {
    // given
    final Caffeines original = new Caffeines(FQCN);
    final int accessLimit = 1;
    original.incrementVisit(IP);
    original.incrementVisit(IP);
    final Caffeines other = new Caffeines(FQCN);

    // when
    assertTrue(other.isOverAccessTime(IP, accessLimit));
  }
}
