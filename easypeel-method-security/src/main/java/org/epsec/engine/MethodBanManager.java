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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * MethodBanManager.
 * @author PENEKhun
 */
public class MethodBanManager implements MethodBanInterface {

  private final Fqcn fqcn;
  private final int times;
  private final String banMessage;
  private static final HashMap<Fqcn, Cache<String, Integer>> accessCache = new HashMap<>();
  private static final HashMap<Fqcn, Cache<String, LocalDateTime>> banCache = new HashMap<>();

  /**
   * Constructor.
   *
   * @param packageWithMethod package with method (e.g. org.epsec.engine.method1)
   * @param times             times to access (must be greater than 1)
   * @param seconds           seconds to access (must be greater than 0)
   * @param banSeconds        ban seconds (must be greater than 0)
   * @param banMessage        ban message (must have text)
   */
  public MethodBanManager(String packageWithMethod, int times, int seconds, int banSeconds,
      String banMessage) {
    this.times = times;
    this.banMessage = banMessage;
    this.fqcn = new Fqcn(packageWithMethod);

    final int defaultMaximumSize = 1_000_000;
    accessCache.putIfAbsent(this.fqcn, Caffeine.newBuilder()
        .maximumSize(defaultMaximumSize)
        .expireAfterWrite(seconds, TimeUnit.SECONDS)
        .build());
    banCache.putIfAbsent(this.fqcn, Caffeine.newBuilder()
        .maximumSize(defaultMaximumSize)
        .expireAfterWrite(banSeconds, TimeUnit.SECONDS)
        .build());
  }

  @Override
  public void checkBanAndAccess(String ipAddress) throws BanException {
    final Cache<String, Integer> accessLog = accessCache.get(fqcn);
    final Cache<String, LocalDateTime> banLog = banCache.get(fqcn);
    if (banLog.getIfPresent(ipAddress) != null) {
      throw new BanException(banMessage);
    }

    final Integer count = accessLog.getIfPresent(ipAddress);
    if (count == null) {
      accessLog.put(ipAddress, 1);
    } else if (count + 1 >= times) {
      banLog.put(ipAddress, LocalDateTime.now());
      accessLog.invalidate(ipAddress);
      throw new BanException(banMessage);
    } else {
      accessLog.put(ipAddress, count + 1);
    }
  }

  /**
   * Clear caches.
   */
  protected void clearCaches() {
    accessCache.get(fqcn).invalidateAll();
    banCache.get(fqcn).invalidateAll();
  }
}
