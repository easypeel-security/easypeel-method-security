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

/**
 * When @MethodBan deny user’s access, This exception will be thrown.
 * @author PENEKhun
 */
public class BanException extends RuntimeException {

  /**
   * Default Constructor.
   *
   * @param message the message to display when the exception is thrown.
   */
  public BanException(String message) {
    super(message);
  }
}
