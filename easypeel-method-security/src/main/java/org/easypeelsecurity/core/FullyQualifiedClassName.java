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

package org.easypeelsecurity.core;

/**
 * Enum Class with recorded 'Fully Qualified Class Name' used in Spring framework frequently.
 *
 * @author PENEKhun
 */
public enum FullyQualifiedClassName {
  GET_MAPPING("org.springframework.web.bind.annotation.GetMapping"),
  POST_MAPPING("org.springframework.web.bind.annotation.PostMapping"),
  PUT_MAPPING("org.springframework.web.bind.annotation.PutMapping"),
  DELETE_MAPPING("org.springframework.web.bind.annotation.DeleteMapping"),
  PATCH_MAPPING("org.springframework.web.bind.annotation.PatchMapping"),
  COMPONENT("org.springframework.stereotype.Component"),
  ENABLE_ASPECT_JAUTO_PROXY("org.springframework.context.annotation.EnableAspectJAutoProxy"),
  JOIN_POINT("org.aspectj.lang.JoinPoint"),
  ASPECT("org.aspectj.lang.annotation.Aspect"),
  ASPECT_METHOD_SIGNATURE("org.aspectj.lang.reflect.MethodSignature"),
  BEFORE("org.aspectj.lang.annotation.Before"),
  HTTP_SERVLET_REQUEST("jakarta.servlet.http.HttpServletRequest"),
  GENERATED("jakarta.annotation.Generated"),
  SERVLET_REQUEST_ATTRIBUTES("org.springframework.web.context.request.ServletRequestAttributes"),
  REQUEST_CONTEXT_HOLDER("org.springframework.web.context.request.RequestContextHolder");

  private final String name;

  FullyQualifiedClassName(String fullyQualifiedClassName) {
    this.name = fullyQualifiedClassName;
  }

  /**
   * Get the name of the Fully Qualified Class Name.
   *
   * @return the name of the FQCN string
   */
  public String getName() {
    return this.name;
  }

  /**
   * Check if the targetFQCN is a Web Annotation (e.g. &#064;GetMapping, &#064;PostMapping, &#064;PutMapping,
   * &#064;DeleteMapping, &#064;PatchMapping)
   *
   * @param targetFQCN the target FQCN string to check
   * @return true if the targetFQCN is a Web Annotation
   */
  public static boolean isWebAnnotation(String targetFQCN) {
    return targetFQCN.startsWith("org.springframework.web.bind.annotation");
  }
}
