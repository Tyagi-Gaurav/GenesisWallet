package com.gw.common.annotations;

import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Processor;
import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateBuilderAnnotationProcessorTest {

    @Test
    void checkGeneratedBuilderForRecord() throws IOException {
        String testPath = "src/test/java";
        File directoryPath = new File(testPath);
        String absolutePath = directoryPath.getAbsolutePath();
        String filePath = absolutePath + "/" + TestRecordWithAnnotation.class.getCanonicalName().replace(".", "/") + ".java";
        File pathToGeneratedBuilder = compile(new GenerateBuilderAnnotationProcessor(), new File(filePath));
        assertThat(java.nio.file.Files.walk(pathToGeneratedBuilder.toPath())
                .anyMatch(path -> path.endsWith(TestRecordWithAnnotation.class.getSimpleName()+"Builder.java"))).isTrue();
    }

    @Test
    void shouldNotGenerateBuilderForRecordWithoutBuilderAnnotation() throws IOException {
        String testPath = "src/test/java";
        File directoryPath = new File(testPath);
        String absolutePath = directoryPath.getAbsolutePath();
        String filePath = absolutePath + "/" + TestRecordWithoutAnnotation.class.getCanonicalName().replace(".", "/") + ".java";
        File pathToGeneratedBuilder = compile(new GenerateBuilderAnnotationProcessor(), new File(filePath));
        assertThat(java.nio.file.Files.walk(pathToGeneratedBuilder.toPath())
                .anyMatch(path -> path.endsWith(TestRecordWithoutAnnotation.class.getSimpleName()+"Builder.java"))).isFalse();
    }

    @Test
    void shouldNotGenerateBuilderForClassWithBuilderAnnotation() throws IOException {
        String testPath = "src/test/java";
        File directoryPath = new File(testPath);
        String absolutePath = directoryPath.getAbsolutePath();
        String filePath = absolutePath + "/" + TestClassWithAnnotation.class.getCanonicalName().replace(".", "/") + ".java";
        compile(new GenerateBuilderAnnotationProcessor(), new File(filePath));
        File pathToGeneratedBuilder = compile(new GenerateBuilderAnnotationProcessor(), new File(filePath));
        assertThat(java.nio.file.Files.walk(pathToGeneratedBuilder.toPath())
                .anyMatch(path -> path.endsWith(TestRecordWithoutAnnotation.class.getSimpleName()+"Builder.java"))).isFalse();
    }

    private File compile(Processor processor, File compilationUnits) throws IOException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);
        Iterable<? extends JavaFileObject> javaFileObjects = fileManager.getJavaFileObjects(compilationUnits);
        File e1 = Files.newTemporaryFolder();
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, List.of(e1));

        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, javaFileObjects);
        task.setProcessors(List.of(processor));
        boolean success = task.call();
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.err.println(diagnostic);
        }
        assertThat(success).as("compile succeeded").isTrue();
        return e1;
    }

}