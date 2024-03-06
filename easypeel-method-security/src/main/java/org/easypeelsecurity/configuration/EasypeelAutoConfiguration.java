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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Main ConfigProperties.
 */
@Configuration
@ConfigurationProperties(prefix = "easypeelsecurity")
public class EasypeelAutoConfiguration {

  private boolean enabled = true;

  private LogOptions log = new LogOptions();

  /**
   * PostConstruct.
   */
  @PostConstruct
  public void init() {
    final StringBuilder sb = new StringBuilder();

    if (this.enabled) {
      sb.append("""
           _____                                       _\s
          |  ___|                                     | |
          | |__   __ _  ___  _   _  _ __    ___   ___ | |
          |  __| / _` |/ __|| | | || '_ \\  / _ \\ / _ \\| |
          | |___| (_| |\\__ \\| |_| || |_) ||  __/|  __/| |
          \\____/ \\__,_||___/ \\__, || .__/  \\___| \\___||_|
                              __/ || |                  \s
                             |___/ |_|                  \s
          Easypeel Security was enabled.
          """);
    }

    if (this.isLogLevelProd() || this.isLogLevelDev()) {
      sb.append("Log output is enabled. The Activated level is ").append(this.log.getLevel());
    }

    System.out.println(sb);
  }

  /**
   * Get Option value of Enabled.
   *
   * @return enabled
   */
  public boolean isEnabled() {
    return this.enabled;
  }

  /**
   * Get Option value of Log.
   *
   * @return dev was enabled
   */
  public boolean isLogLevelDev() {
    return this.log.isEnabled() && this.log.getLevel() == LogLevel.DEV;
  }

  /**
   * Set Option value of Enabled.
   *
   * @return prod was enabled
   */
  public boolean isLogLevelProd() {
    return this.log.isEnabled() && this.log.getLevel() == LogLevel.PROD;
  }

  /**
   * Set Option value of Enabled.
   *
   * @param enabled enabled
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Get Option value of Log.
   *
   * @param log logOption Object
   */
  public void setLog(LogOptions log) {
    this.log = log;
  }

  /**
   * Get Option value of Log.
   *
   * @return logOption Object
   */
  public LogOptions getLog() {
    return log;
  }
}
