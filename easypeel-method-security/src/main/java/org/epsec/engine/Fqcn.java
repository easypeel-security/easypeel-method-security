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

import org.springframework.util.StringUtils;

/**
 * Fully Qualified Class Name Data Structure.
 * @author PENEKhun
 */
public final class Fqcn {

  private final String fullPackage;
  private final String methodName;

  /**
   * Constructor.
   * @param fullPackage full package name (e.g. org.epsec.engine)
   * @param methodName  method name (e.g. method1)
   */
  public Fqcn(String fullPackage, String methodName) {
    if (!(StringUtils.hasText(fullPackage) && StringUtils.hasText(methodName))) {
      throw new IllegalArgumentException("Cannot create Fqcn with empty string");
    }

    this.fullPackage = fullPackage;
    this.methodName = methodName;
  }

  /**
   * Get full package name with method.
   */
  @Override
  public String toString() {
    return fullPackage + "." + methodName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final Fqcn fqcn = (Fqcn) o;

    if (!fullPackage.equals(fqcn.fullPackage)) {
      return false;
    }
    return methodName.equals(fqcn.methodName);
  }

  @Override
  public int hashCode() {
    int result = fullPackage.hashCode();
    result = 31 * result + methodName.hashCode();
    return result;
  }
}
