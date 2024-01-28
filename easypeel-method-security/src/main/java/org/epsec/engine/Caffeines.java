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

import org.springframework.util.StringUtils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

/**
 * MemCache Factory.
 * @author PENEKhun
 */
public class Caffeines implements MemCache {

  private final Fqcn fqcn;
  private final int times;
  private final int seconds;
  private final int banSeconds;
  private final String banMessage;
  private static final HashMap<Fqcn, Cache<String, Integer>> accessCache = new HashMap<>();
  private static final HashMap<Fqcn, Cache<String, LocalDateTime>> banCache = new HashMap<>();
  private int defaultMaximumSize = 1_000_00;

  /**
   * Constructor.
   */
  public Caffeines(String fullPackage, String methodName, int times, int seconds, int banSeconds,
      String banMessage) {
    assert times >= 2;
    assert seconds >= 1;
    assert banSeconds >= 1;
    assert StringUtils.hasText(fullPackage);
    assert StringUtils.hasText(methodName);
    assert StringUtils.hasText(banMessage);

    this.times = times;
    this.seconds = seconds;
    this.banSeconds = banSeconds;
    this.banMessage = banMessage;
    this.fqcn = new Fqcn(fullPackage, methodName);

    accessCache.putIfAbsent(fqcn, Caffeine.newBuilder()
        .maximumSize(this.defaultMaximumSize)
        .expireAfterWrite(this.seconds, TimeUnit.SECONDS)
        .build());

    banCache.putIfAbsent(fqcn, Caffeine.newBuilder()
        .maximumSize(this.defaultMaximumSize)
        .expireAfterWrite(this.banSeconds, TimeUnit.SECONDS)
        .evictionListener((key, value, cause) -> {
          if (cause.wasEvicted()) {
            accessCache.get(fqcn).invalidate(String.valueOf(key));
          }
        })
        .build());
  }

  private void changeDefaultMaximumSize(int size) {
    this.defaultMaximumSize = size;
  }

  @Override
  public void checkBanAndAccess(String ipAddress) throws BanException {
    assert StringUtils.hasText(ipAddress);

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
