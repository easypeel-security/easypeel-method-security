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

import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * MemCache Factory.
 * @author PENEKhun
 */
public class Caffeines implements MemCache {

  private final Fqcn fullyQualifiedClassName;
  private static final HashMap<Fqcn, Cache<String, Integer>> caches = new HashMap<>();
  private int defaultMaximumSize = 1_000_00;

  /**
   * Constructor.
   * @param fullyQualifiedClassName Fully Qualified Class Name
   */
  public Caffeines(Fqcn fullyQualifiedClassName) {
    this.fullyQualifiedClassName = fullyQualifiedClassName;
  }

  private void changeDefaultMaximumSize(int size) {
    this.defaultMaximumSize = size;
  }

  /**
   * Create a new cache if it does not exist.
   *
   * @return a new cache
   */
  private Cache<String, Integer> getOrElseCreate() {
    if (caches.containsKey(this.fullyQualifiedClassName)) {
      return caches.get(this.fullyQualifiedClassName);
    }

    final Cache<String, Integer> cache = Caffeine.newBuilder()
        .maximumSize(this.defaultMaximumSize)
        .build();
    caches.put(this.fullyQualifiedClassName, cache);
    return cache;
  }

  @Override
  public boolean isOverAccessTime(String ipAddress, int accessTime) {
    assert StringUtils.hasText(ipAddress);

    final Cache<String, Integer> cache = getOrElseCreate();
    Integer count = cache.getIfPresent(ipAddress);
    if (count == null) {
      count = 0;
    }

    return count > accessTime;
  }

  /**
   * Increment the visit count.
   *
   * @param ipAddress IP Address
   */
  public void incrementVisit(String ipAddress) {
    assert StringUtils.hasText(ipAddress);

    final Cache<String, Integer> cache = getOrElseCreate();
    Integer count = cache.getIfPresent(ipAddress);
    if (count == null) {
      count = 0;
    }

    cache.put(ipAddress, count + 1);
  }
}
