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

package org.easypeelsecurity.engine;

import static org.awaitility.Awaitility.await;
import static org.easypeelsecurity.configuration.fixture.ConfigurationFixtures.createEasypeelAutoConfiguration;
import static org.easypeelsecurity.configuration.fixture.ConfigurationFixtures.createLogOptions;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeUnit;

import org.easypeelsecurity.configuration.EasypeelAutoConfiguration;
import org.easypeelsecurity.configuration.LogLevel;
import org.easypeelsecurity.configuration.LogOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class MethodBanManagerTest {

  final String testIp = "Test";

  @ParameterizedTest
  @CsvSource({
      "true, DEV",
      "true, PROD",
      "false, DEV",
      "false, PROD"
  })
  void checkBanAndAccessThrowsWhenWillExceedAccess(boolean logEnable, LogLevel logLevel) {
    // given
    final LogOptions logOptions = createLogOptions(logEnable, logLevel);
    final EasypeelAutoConfiguration configuration = createEasypeelAutoConfiguration();
    configuration.setLog(logOptions);

    final MethodBanManager caffeines =
        new MethodBanManager("org.test.method", 3, 2, 1, "You are banned", configuration);
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);
    caffeines.checkBanAndAccess(testIp);

    // when & then
    assertThrows(BanException.class,
        () -> caffeines.checkBanAndAccess(testIp),
        "Your are banned");
  }

  @Test
  void checkBanAndAccessThrowsWhenExceedAccess() {
    // given
    final MethodBanManager caffeines =
        new MethodBanManager("org.test.method", 3, 2, 1, "You are banned", createEasypeelAutoConfiguration());
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);
    caffeines.checkBanAndAccess(testIp);
    try {
      caffeines.checkBanAndAccess(testIp);
    } catch (BanException e) {
      // ignore
    }

    // when & then
    assertThrows(BanException.class,
        () -> caffeines.checkBanAndAccess(testIp),
        "Your are banned");
  }

  @Test
  void checkBanAndAccessDoesNotThrows() {
    // given
    final MethodBanManager caffeines =
        new MethodBanManager("org.test.method", 3, 1, 1, "test", createEasypeelAutoConfiguration());
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);

    // when & then
    assertDoesNotThrow(() -> caffeines.checkBanAndAccess(testIp));
  }

  @Test
  void banCacheExpireWorking() {
    // given
    final MethodBanManager caffeines =
        new MethodBanManager("org.test.method2", 2, 10, 1, "test", createEasypeelAutoConfiguration());
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);

    // when & then
    Assertions.assertThrows(
        BanException.class,
        () -> caffeines.checkBanAndAccess(testIp)
    );

    /*
      A slight time correction is required for accurate testing.
     */
    await().atMost(1_100_000, TimeUnit.MICROSECONDS)
        .untilAsserted(() ->
            assertDoesNotThrow(() -> caffeines.checkBanAndAccess(testIp))
        );
  }

  @Test
  void configurationDisableWorking() {
    // given
    final EasypeelAutoConfiguration configuration = createEasypeelAutoConfiguration();
    configuration.setEnabled(false);

    final MethodBanManager caffeines =
        new MethodBanManager("org.test.method", 2, 10, 10, "test", configuration);
    caffeines.clearCaches();
    caffeines.checkBanAndAccess(testIp);
    caffeines.checkBanAndAccess(testIp);
    caffeines.checkBanAndAccess(testIp);

    // when & then
    assertDoesNotThrow(() -> caffeines.checkBanAndAccess(testIp));
  }
}
