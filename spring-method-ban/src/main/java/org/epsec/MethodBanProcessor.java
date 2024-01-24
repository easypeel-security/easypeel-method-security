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

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * MethodBan Annotation Processor.
 *
 * @author PENEKhun
 */
public class MethodBanProcessor extends AbstractProcessor {

  private boolean isAlreadyProcessed;

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(MethodBan.class.getName());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MethodBan.class);
    for (Element element : elements) {
      if (isAlreadyProcessed) {
        break;
      }

      final MethodBan methodBan = element.getAnnotation(MethodBan.class);
      if (methodBan.times() < 1) {
        processingEnv.getMessager().printMessage(ERROR, "times must be greater than 0", element);
      }

      if (methodBan.seconds() < 1) {
        processingEnv.getMessager().printMessage(ERROR, "seconds must be greater than 0", element);
      }

      if (methodBan.banSeconds() < 1) {
        processingEnv.getMessager().printMessage(ERROR, "banSeconds must be greater than 0", element);
      }

      if (!element.getModifiers().contains(Modifier.PUBLIC)) {
        processingEnv.getMessager().printMessage(ERROR, "Method must be public", element);
      }

      if (!isUsedWithMappingAnnotation(element)) {
        processingEnv.getMessager().printMessage(ERROR,
            "@MethodBan must be used with a Spring Mapping Annotation (@GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping)",
            element);
      }

      processingEnv.getMessager().printMessage(NOTE, "MethodBan Annotation Processing now ", element);

      final ClassName component = ClassName.bestGuess("org.springframework.stereotype.Component");
      final ClassName enableAspectJAutoProxy =
          ClassName.bestGuess("org.springframework.context.annotation.EnableAspectJAutoProxy");
      final TypeSpec enableAopClass = TypeSpec.classBuilder("EnableAopClass" + System.nanoTime())
          .addModifiers(Modifier.PUBLIC)
          .addAnnotation(component)
          .addAnnotation(enableAspectJAutoProxy)
          .build();

      final Filer filer = processingEnv.getFiler();
      final String fullPackageName = element.getEnclosingElement().toString();
      final String originalPackageName = fullPackageName.substring(0, fullPackageName.lastIndexOf("."));

      try {
        JavaFile.builder(originalPackageName, enableAopClass)
            .build()
            .writeTo(filer);
      } catch (IOException e) {
        processingEnv.getMessager().printMessage(ERROR, "Fatal error", element);
      }

      // annotation spec
      final ClassName before = ClassName.bestGuess("org.aspectj.lang.annotation.Before");
      final AnnotationSpec annotationSpec = AnnotationSpec.builder(before)
          .addMember("value", "$S", "@annotation(org.epsec.MethodBan)")
          .build();

      // new Method
      final MethodSpec methodSpec = MethodSpec.methodBuilder("beforeMethodBan" + System.nanoTime())
          .addModifiers(Modifier.PUBLIC)
          .addAnnotation(annotationSpec)
          .addParameter(ClassName.bestGuess("org.aspectj.lang.JoinPoint"), "joinPoint")
          .addStatement("System.out.println(\"hi~\")")
          .build();

      // new Class
      final TypeSpec classSpec = TypeSpec.classBuilder("MethodBanAspect" + System.nanoTime())
          .addModifiers(Modifier.PUBLIC)
          .addAnnotation(ClassName.bestGuess("org.aspectj.lang.annotation.Aspect"))
          .addAnnotation(ClassName.bestGuess("org.springframework.stereotype.Component"))
          .addMethod(methodSpec)
          .build();

      // write file
      try {
        JavaFile.builder(originalPackageName, classSpec)
            .build()
            .writeTo(filer);
      } catch (IOException e) {
        processingEnv.getMessager().printMessage(ERROR, "Fatal error", element);
      }

      isAlreadyProcessed = true;
    }
    return true;
  }

  private boolean isUsedWithMappingAnnotation(Element element) {
    final List<String> springAnnotations = Arrays.asList(
        "org.springframework.web.bind.annotation.GetMapping",
        "org.springframework.web.bind.annotation.PostMapping",
        "org.springframework.web.bind.annotation.PutMapping",
        "org.springframework.web.bind.annotation.DeleteMapping",
        "org.springframework.web.bind.annotation.PatchMapping"
    );

    boolean isUsedWithMappingAnnotaion = false;
    for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
      if (springAnnotations.contains(mirror.getAnnotationType().toString())) {
        isUsedWithMappingAnnotaion = true;
        break;
      }
    }
    return isUsedWithMappingAnnotaion;
  }
}
