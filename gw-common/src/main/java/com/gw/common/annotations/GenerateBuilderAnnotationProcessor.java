package com.gw.common.annotations;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@SupportedAnnotationTypes("com.gw.common.annotations.GenerateBuilder")
@SupportedSourceVersion(SourceVersion.RELEASE_17)
public class GenerateBuilderAnnotationProcessor extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element e : roundEnv.getElementsAnnotatedWith(GenerateBuilder.class)) {
            if (e.getKind() != ElementKind.RECORD) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "Can only be used on a record", e);
                continue;
            }
            var clazz = e.getEnclosingElement();
            try {
                JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(e.getSimpleName() + "Builder");
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                        "Creating Builder for " + e.getSimpleName());
                try (var w = sourceFile.openWriter();
                     var pw = new PrintWriter(w)) {
                    var classBuilder = new StringBuilder();
                    classBuilder.append("package ").append(clazz).append(";\n")
                            .append("public class ").append(e.getSimpleName()).append("Builder {").append("\n\n");

                    //Create field
                    for (Element childElements : e.getEnclosedElements()) {
                        if (childElements.getKind() == ElementKind.FIELD) {
                            TypeMirror type = childElements.asType();
                            var name = childElements.getSimpleName().toString();
                            classBuilder.append("private ").append(type).append(" ").append(name).append(";\n");

                            //Create setter for the fields
                            classBuilder.append("public void set").append(capitalize(name))
                                    .append("(").append(type).append(" ").append("new").append(name).append(") {\n")
                                    .append("this.").append(name).append("=").append("new").append(name).append(";\n")
                                    .append("}\n");
                        }
                    }

                    //Create final builder method
                    classBuilder.append("public ").append(e.getSimpleName()).append(" build() {\n");
                    classBuilder.append("return new ").append(e.getSimpleName()).append("(\n");
                    List<? extends Element> enclosedElements = e.getEnclosedElements();
                    var fieldCreated = false;
                    for (Element childElements : enclosedElements) {
                        if (childElements.getKind() == ElementKind.FIELD) {
                            var name = childElements.getSimpleName().toString();
                            if (fieldCreated) {
                                classBuilder.append(",\n");
                            }
                            classBuilder.append(name);
                            fieldCreated = true;
                        }
                    }
                    classBuilder.append(");\n").append("} ").append("\n");

                    classBuilder.append("}");
                    pw.println(classBuilder);
                    pw.flush();
                }
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        ex.toString());
            }
        }
        return true;
    }

    private static String capitalize(String name) {
        char[] c = name.toCharArray();
        c[0] = Character.toUpperCase(c[0]);
        return new String(c);
    }
}
