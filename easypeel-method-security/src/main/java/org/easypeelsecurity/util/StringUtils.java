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

package org.easypeelsecurity.util;

/**
 * String utilities.
 */
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class StringUtils {

  /**
   * Check if the string is null or empty.
   * @param str the string to check.
   * @return true if the string is null or empty, false otherwise.
   */
  public static boolean hasText(String str) {
      return str != null && !str.trim().isEmpty();
    }
}
