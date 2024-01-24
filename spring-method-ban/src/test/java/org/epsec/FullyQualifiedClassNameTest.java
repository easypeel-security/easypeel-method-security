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

package org.epsec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FullyQualifiedClassNameTest {

  @Test
  void getNameReturnWell() {
    assertEquals("org.springframework.web.bind.annotation.GetMapping",
        FullyQualifiedClassName.GET_MAPPING.getName());
    assertEquals("org.springframework.web.bind.annotation.PostMapping",
        FullyQualifiedClassName.POST_MAPPING.getName());
    assertEquals("org.springframework.web.bind.annotation.PutMapping",
        FullyQualifiedClassName.PUT_MAPPING.getName());
    assertEquals("org.springframework.web.bind.annotation.DeleteMapping",
        FullyQualifiedClassName.DELETE_MAPPING.getName());
    assertEquals("org.springframework.web.bind.annotation.PatchMapping",
        FullyQualifiedClassName.PATCH_MAPPING.getName());
    assertEquals("org.springframework.stereotype.Component", FullyQualifiedClassName.COMPONENT.getName());
    assertEquals("org.springframework.context.annotation.EnableAspectJAutoProxy",
        FullyQualifiedClassName.ENABLE_ASPECT_JAUTO_PROXY.getName());
    assertEquals("org.aspectj.lang.JoinPoint", FullyQualifiedClassName.JOIN_POINT.getName());
    assertEquals("org.aspectj.lang.annotation.Aspect", FullyQualifiedClassName.ASPECT.getName());
    assertEquals("org.aspectj.lang.annotation.Before", FullyQualifiedClassName.BEFORE.getName());
  }

  @Test
  void isWebAnnotationHappy() {
    assertTrue(FullyQualifiedClassName.isWebAnnotation("org.springframework.web.bind.annotation.GetMapping"));
    assertTrue(FullyQualifiedClassName.isWebAnnotation("org.springframework.web.bind.annotation.PostMapping"));
    assertTrue(FullyQualifiedClassName.isWebAnnotation("org.springframework.web.bind.annotation.PutMapping"));
    assertTrue(
        FullyQualifiedClassName.isWebAnnotation("org.springframework.web.bind.annotation.DeleteMapping"));
    assertTrue(FullyQualifiedClassName.isWebAnnotation("org.springframework.web.bind.annotation.PatchMapping"));
  }

  @Test
  void isWebAnnotationSad() {
    assertFalse(FullyQualifiedClassName.isWebAnnotation("org.springframework.stereotype.Component"));
    assertFalse(FullyQualifiedClassName.isWebAnnotation(
        "org.springframework.context.annotation.EnableAspectJAutoProxy"));
    assertFalse(FullyQualifiedClassName.isWebAnnotation("org.aspectj.lang.JoinPoint"));
    assertFalse(FullyQualifiedClassName.isWebAnnotation("org.aspectj.lang.annotation.Aspect"));
    assertFalse(FullyQualifiedClassName.isWebAnnotation("org.aspectj.lang.annotation.Before"));
  }
}
