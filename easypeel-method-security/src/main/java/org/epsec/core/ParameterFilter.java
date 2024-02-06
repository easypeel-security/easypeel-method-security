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

package org.epsec.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation allows you to add Spring Security's Authentication-based or any other form of blocking filter
 * of your choice.
 *
 * @author PENEKhun
 * @see MethodBan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({})
public @interface ParameterFilter {

  /**
   * Enable or disable the filter.
   */
  boolean isEnabled() default true;

  /**
   * Variable names for parameters that can identify the user other than IP (authentication object or any
   * object).
   */
  String target() default "";
}
