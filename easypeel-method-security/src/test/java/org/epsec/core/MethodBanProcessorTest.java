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

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import java.util.stream.Stream;

import javax.tools.JavaFileObject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;

@SuppressWarnings("checkstyle:FinalLocalVariable")
class MethodBanProcessorTest {

  @Test
  void compileHappy() {
    JavaFileObject src = JavaFileObjects.forResource("before/MethodBanHappy.java");
    Compilation compilation = javac()
        .withProcessors(new MethodBanProcessor())
        .compile(src);
    assertThat(compilation).succeededWithoutWarnings();
  }

  @ParameterizedTest
  @MethodSource("wrongCaseSet")
  void testMethodBanProcessor(String fileName, String expectedErrorMessage) {
    JavaFileObject src = JavaFileObjects.forResource("before/" + fileName);
    Compilation compilation = javac()
        .withProcessors(new MethodBanProcessor())
        .compile(src);
    assertThat(compilation).hadErrorContaining(expectedErrorMessage);
  }

  @SuppressWarnings("checkstyle:LineLength")
  static Stream<Arguments> wrongCaseSet() {
    return Stream.of(
        Arguments.of("MethodBanMustBeUsedWithMapping.java",
            "@MethodBan must be used with a Spring Mapping Annotation (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping)"),
        Arguments.of("MethodBanTimesMustBeGreaterThanOne.java",
            "times must be greater than 1"),
        Arguments.of("MethodBanSecondsMustBeGreaterThanZero.java",
            "seconds must be greater than 0"),
        Arguments.of("MethodBanBanSecondsMustBeGreaterThanZero.java",
            "banSeconds must be greater than 0"),
        Arguments.of("MethodBanMustBePublicMethod.java",
            "Method must be public")
    );
  }
}
