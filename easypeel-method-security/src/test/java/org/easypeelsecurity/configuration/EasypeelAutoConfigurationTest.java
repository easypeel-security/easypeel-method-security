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

package org.easypeelsecurity.configuration;

import static org.easypeelsecurity.configuration.fixture.ConfigurationFixtures.createLogOptions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class EasypeelAutoConfigurationTest {

  @ParameterizedTest
  @CsvSource({
      "true, DEV, true",
      "false, DEV, false",
      "false, PROD, false",
      "true, PROD, false",
  })
  void isLogLevelDev(boolean logEnable, LogLevel logLevel, boolean expected) {
    // given
    final EasypeelAutoConfiguration configuration = new EasypeelAutoConfiguration();
    final LogOptions logOptions = createLogOptions(logEnable, logLevel);
    configuration.setLog(logOptions);

    // when
    final boolean result = configuration.isLogLevelDev();

    // then
    assertEquals(expected, result);
  }

  @ParameterizedTest
  @CsvSource({
      "false, DEV, false",
      "true, DEV, false",
      "false, PROD, false",
      "true, PROD, true",
  })
  void isLogLevelProd(boolean logEnable, LogLevel logLevel, boolean expected) {
    // given
    final EasypeelAutoConfiguration configuration = new EasypeelAutoConfiguration();
    final LogOptions logOptions = createLogOptions(logEnable, logLevel);
    configuration.setLog(logOptions);

    // when
    final boolean result = configuration.isLogLevelProd();

    // then
    assertEquals(expected, result);
  }

  @Test
  void logGetter() {
    // given
    final EasypeelAutoConfiguration configuration = new EasypeelAutoConfiguration();
    final LogOptions logOptions = createLogOptions(true, LogLevel.DEV);

    // when
    configuration.setLog(logOptions);

    // then
    assertEquals(logOptions, configuration.getLog());
  }

  @Test
  void defaultEnable() {
    // given
    final EasypeelAutoConfiguration configuration = new EasypeelAutoConfiguration();
    configuration.init();

    // when & then
    assertTrue(configuration.isEnabled());
  }

  @Test
  void defaultLogDisable() {
    // given
    final EasypeelAutoConfiguration configuration = new EasypeelAutoConfiguration();
    configuration.init();

    // when & then
    assertFalse(configuration.isLogLevelDev() && configuration.isLogLevelProd());
  }

  @ParameterizedTest
  @CsvSource({"true", "false"})
  void enableSetterGetter(boolean enabled) {
    // given
    final EasypeelAutoConfiguration configuration = new EasypeelAutoConfiguration();
    configuration.init();

    // when
    configuration.setEnabled(enabled);

    // then
    assertEquals(enabled, configuration.isEnabled());
  }
}
