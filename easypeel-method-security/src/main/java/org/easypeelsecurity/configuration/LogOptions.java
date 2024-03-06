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

import org.springframework.context.annotation.Configuration;

/**
 * LogOptions.
 */
@Configuration
public class LogOptions {

  private boolean enabled;
  private LogLevel level = LogLevel.PROD;

  /**
   * Get Option value of Enabled.
   *
   * @return enabled
   */
  public boolean isEnabled() {
    return enabled;
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
   * Get Option value of Log Level.
   *
   * @return Log level enum for specifying log display
   */
  public LogLevel getLevel() {
    return level;
  }

  /**
   * Set Option value of Log Level.
   *
   * @param level Log level enum for specifying log display
   */
  public void setLevel(LogLevel level) {
    this.level = level;
  }
}
