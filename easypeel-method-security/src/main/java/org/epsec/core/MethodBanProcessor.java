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

import static javax.tools.Diagnostic.Kind.ERROR;
import static org.epsec.core.FullyQualifiedClassName.ASPECT;
import static org.epsec.core.FullyQualifiedClassName.ASPECT_METHOD_SIGNATURE;
import static org.epsec.core.FullyQualifiedClassName.BEFORE;
import static org.epsec.core.FullyQualifiedClassName.COMPONENT;
import static org.epsec.core.FullyQualifiedClassName.ENABLE_ASPECT_JAUTO_PROXY;
import static org.epsec.core.FullyQualifiedClassName.GENERATED;
import static org.epsec.core.FullyQualifiedClassName.HTTP_SERVLET_REQUEST;
import static org.epsec.core.FullyQualifiedClassName.JOIN_POINT;
import static org.epsec.core.FullyQualifiedClassName.REQUEST_CONTEXT_HOLDER;
import static org.epsec.core.FullyQualifiedClassName.SERVLET_REQUEST_ATTRIBUTES;
import static org.epsec.core.FullyQualifiedClassName.isWebAnnotation;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import org.epsec.engine.MethodBanManager;
import org.epsec.util.StringUtils;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.CodeBlock.Builder;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * MethodBan Annotation Processor.
 *
 * @author PENEKhun
 */
@SuppressWarnings("all")
public class MethodBanProcessor extends AbstractProcessor {

  private boolean isAlreadyProcessed;

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return Set.of(MethodBan.class.getName());
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    if (processingEnv.getSourceVersion().compareTo(SourceVersion.RELEASE_17) < 0) {
      processingEnv.getMessager().printMessage(ERROR, "MethodBan is only supported in Java 17 or higher.");
    }

    return SourceVersion.latestSupported();
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MethodBan.class);
    for (Element element : elements) {
      if (isAlreadyProcessed) {
        break;
      }

      checkValidMethodBan(element);
      processMethodBan(element);
      isAlreadyProcessed = true;
    }
    return true;
  }

  private void processMethodBan(Element element) {
    MethodBan methodBan = element.getAnnotation(MethodBan.class);
    ParameterFilter banWith = methodBan.additionalFilter();
    if (banWith.isEnabled()) {
      checkValidBanWith(element, methodBan.additionalFilter().target());
    }

    TypeSpec enableAopClass = generateEnableAopClass(element);
    TypeSpec banAspectClass = generateMethodBanAspect(element, banWith);

    // get the package name
    String fullPackageName = element.getEnclosingElement().toString();
    saveJavaFile(fullPackageName, enableAopClass);
    saveJavaFile(fullPackageName, banAspectClass);
  }

  private void checkValidBanWith(Element element, String authenticationArgumentName) {
    if (!StringUtils.hasText(authenticationArgumentName)) {
      processingEnv.getMessager().printMessage(ERROR, "name must be provided", element);
    }

    ExecutableElement methodElement = (ExecutableElement) element;
    List<? extends VariableElement> parameters = methodElement.getParameters();

    boolean isExist = false;
    for (VariableElement parameter : parameters) {
      if (parameter.getSimpleName().toString().equals(authenticationArgumentName)) {
        isExist = true;
        break;
      }
    }

    if (!isExist) {
      processingEnv.getMessager().printMessage(ERROR,
          "Authentication argument is not exist in method parameters. Please check name argument.", element);
    }
  }

  private TypeSpec generateMethodBanAspect(Element element, ParameterFilter banWith) {
    ClassName before = ClassName.bestGuess(BEFORE.getName());
    AnnotationSpec annotationSpec = AnnotationSpec.builder(before)
        .addMember("value", "$S", "@annotation(%s)".formatted(ClassName.get(MethodBan.class)))
        .build();

    MethodSpec getUserIpMethodSpec = MethodSpec.methodBuilder("getUserIp")
        .addModifiers(Modifier.PRIVATE)
        .returns(String.class)
        .addCode(CodeBlock.builder()
            .addStatement("$T request = (($T) $T.getRequestAttributes()).getRequest()",
                ClassName.bestGuess(HTTP_SERVLET_REQUEST.getName()),
                ClassName.bestGuess(SERVLET_REQUEST_ATTRIBUTES.getName()),
                ClassName.bestGuess(REQUEST_CONTEXT_HOLDER.getName()))
            .addStatement("$T xForwardedForHeader = $L.getHeader($S)", String.class, "request",
                "X-Forwarded-For")
            .beginControlFlow("if ($L == null)", "xForwardedForHeader")
            .addStatement("return $L.getRemoteAddr()", "request")
            .endControlFlow()
            .addStatement("return $L.split($S)[0]", "xForwardedForHeader", ",")
            .build())
        .build();

    MethodBan methodBan = element.getAnnotation(MethodBan.class);
    Builder codes = CodeBlock.builder()
        .addStatement("$T packageName = $L.getSignature().getDeclaringTypeName()", String.class, "joinPoint")
        .addStatement("$T methodName = $L.getSignature().getName()", String.class, "joinPoint")
        .addStatement("$T cache = new $T(packageName + methodName, $L, $L, $L, $S)",
            MethodBanManager.class, MethodBanManager.class, methodBan.times(), methodBan.seconds(),
            methodBan.banSeconds(), methodBan.banMessage());

    if (banWith.isEnabled()) {
      codes.addStatement("$T banWith", String.class)
          .addStatement("$T[] parameterNames = (($T) $L.getSignature()).getParameterNames()",
              String.class, ClassName.bestGuess(ASPECT_METHOD_SIGNATURE.getName()), "joinPoint")
          .addStatement("$T[] arguments = $L.getArgs()", Object.class, "joinPoint")
          .beginControlFlow("for (int i = 0; i < parameterNames.length; i++)")
          .beginControlFlow("if (parameterNames[i].equals($S))", banWith.target())
          .addStatement("banWith = arguments[i].toString()")
          .addStatement("System.out.println(banWith)")
          .addStatement("$L.checkBanAndAccess($L() + banWith)", "cache", "getUserIp")
          .addStatement("break")
          .endControlFlow()
          .endControlFlow();
    } else {
      codes.addStatement("$L.checkBanAndAccess($L())", "cache", "getUserIp");
    }

    MethodSpec methodSpec = MethodSpec.methodBuilder("beforeMethodBan" + System.nanoTime())
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(annotationSpec)
        .addParameter(ClassName.bestGuess(JOIN_POINT.getName()), "joinPoint")
        .addCode(codes.build())
        .build();

    TypeSpec classSpec = TypeSpec.classBuilder("MethodBanAspect" + System.nanoTime())
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(ClassName.bestGuess(ASPECT.getName()))
        .addAnnotation(ClassName.bestGuess(COMPONENT.getName()))
        .addMethod(methodSpec)
        .addMethod(getUserIpMethodSpec)
        .build();

    return classSpec;
  }

  private TypeSpec generateEnableAopClass(Element element) {
    return TypeSpec.classBuilder("EnableAopClass" + System.nanoTime())
        .addModifiers(Modifier.PUBLIC)
        .addAnnotation(ClassName.bestGuess(COMPONENT.getName()))
        .addAnnotation(ClassName.bestGuess(ENABLE_ASPECT_JAUTO_PROXY.getName()))
        .build();
  }

  private void saveJavaFile(String fullPackageName, TypeSpec classSpec) {
    Filer filer = processingEnv.getFiler();
    String originalPackageName = fullPackageName.substring(0, fullPackageName.lastIndexOf("."));
    try {
      classSpec = classSpec.toBuilder()
          .addAnnotation(AnnotationSpec.builder(ClassName.bestGuess(GENERATED.getName()))
              .addMember("value", "$S", MethodBanProcessor.class.getName())
              .build())
          .build();

      JavaFile.builder(originalPackageName, classSpec)
          .build()
          .writeTo(filer);
    } catch (IOException e) {
      processingEnv.getMessager().printMessage(ERROR, "Fatal error" + e.getMessage());
    }
  }

  private void checkValidMethodBan(Element element) {
    MethodBan methodBan = element.getAnnotation(MethodBan.class);
    if (methodBan.times() <= 1) {
      processingEnv.getMessager().printMessage(ERROR, "times must be greater than 1", element);
    }

    if (methodBan.seconds() <= 0) {
      processingEnv.getMessager().printMessage(ERROR, "seconds must be greater than 0", element);
    }

    if (methodBan.banSeconds() <= 0) {
      processingEnv.getMessager().printMessage(ERROR, "banSeconds must be greater than 0", element);
    }

    if (!element.getModifiers().contains(Modifier.PUBLIC)) {
      processingEnv.getMessager().printMessage(ERROR, "Method must be public", element);
    }

    if (!isUsedWithMappingAnnotation(element)) {
      processingEnv.getMessager()
          .printMessage(ERROR, "@MethodBan must be used with a Spring Mapping Annotation " +
              "(@GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping)", element);
    }
  }

  private boolean isUsedWithMappingAnnotation(Element element) {
    boolean isUsedWithMappingAnnotaion = false;
    for (AnnotationMirror mirror : element.getAnnotationMirrors()) {
      if (isWebAnnotation(mirror.getAnnotationType().toString())) {
        isUsedWithMappingAnnotaion = true;
        break;
      }
    }
    return isUsedWithMappingAnnotaion;
  }
}
