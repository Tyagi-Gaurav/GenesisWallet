package com.gw.common.annotations;

import org.assertj.core.util.Files;
import org.junit.jupiter.api.Test;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GenerateBuilderAnnotationProcessorTest {

    @Test
    void checkGeneratedBuilder() throws IOException {
        String path = "src/test/java";
        File directoryPath = new File(path);
        String absolutePath = directoryPath.getAbsolutePath();
        String filePath = absolutePath + "/" + TestRecord.class.getCanonicalName().replace(".", "/") + ".java";
        compile(new GenerateBuilderAnnotationProcessor(), new File(filePath));
    }

    private void compile(Processor processor, File compilationUnits) throws IOException {
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
    }

}