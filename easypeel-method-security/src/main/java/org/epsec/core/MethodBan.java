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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MethodBan Annotation.
 *
 * @author PENEKhun
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodBan {

  /**
   * The number of times the method can be called within the time period.
   */
  int times() default 5;

  /**
   * The time period in seconds.
   */
  int seconds() default 60;

  /**
   * The number of seconds to ban the user from calling the method.
   */
  int banSeconds() default 60;

  /**
   * The message to pass when the user is banned.
   */
  String banMessage() default "You have been banned from calling this method. Please try again later.";
}
