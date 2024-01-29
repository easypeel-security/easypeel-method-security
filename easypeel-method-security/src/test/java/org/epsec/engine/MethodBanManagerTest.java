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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class MethodBanManagerTest {

  final String testIp = "Test";

  @Test
  void checkBanAndAccessThrowsWhenExceedAccess() {
    // given
    final MethodBanManager caffeines = new MethodBanManager("org.test.method", 3, 2, 1, "You are banned");
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);
    caffeines.checkBanAndAccess(testIp);

    // when & then
    assertThrows(BanException.class,
        () -> caffeines.checkBanAndAccess(testIp),
        "Your are banned");
  }

  @Test
  void checkBanAndAccessDoesNotThrows() {
    // given
    final MethodBanManager caffeines = new MethodBanManager("org.test.method", 3, 1, 1, "test");
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);

    // when & then
    assertDoesNotThrow(() -> caffeines.checkBanAndAccess(testIp));
  }

  @Test
  void banCacheExpireWorking() throws Exception {
    // given
    final MethodBanManager caffeines = new MethodBanManager("org.test.method", 2, 10, 1, "test");
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);
    try {
      caffeines.checkBanAndAccess(testIp);
    } catch (BanException e) {
      // ignore
    }

    // when & then
    Thread.sleep(1_100);
    assertDoesNotThrow(() -> caffeines.checkBanAndAccess(testIp));
  }
}
