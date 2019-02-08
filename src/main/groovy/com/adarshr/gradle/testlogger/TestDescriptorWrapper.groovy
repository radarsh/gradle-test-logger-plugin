package com.adarshr.gradle.testlogger

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.tasks.testing.TestDescriptor

import static com.adarshr.gradle.testlogger.util.RendererUtils.escape

@CompileStatic
class TestDescriptorWrapper {

    @Delegate
    private final TestDescriptor testDescriptor
    private final TestLoggerExtension testLoggerExtension

    TestDescriptorWrapper(TestDescriptor testDescriptor, TestLoggerExtension testLoggerExtension) {
        this.testDescriptor = testDescriptor
        this.testLoggerExtension = testLoggerExtension
    }

    String getClassName() {
        escape(testDescriptor.className)
    }

    @CompileDynamic
    String getClassDisplayName() {
        escape(testDescriptor.properties.classDisplayName ?: testDescriptor.className)
    }

    @CompileDynamic
    String getDisplayName() {
        escape(testDescriptor.properties.displayName ?: testDescriptor.name)
    }

    String getSuiteKey() {
        "${testDescriptor.className}:${testDescriptor.className}"
    }

    String getTestKey() {
        "${testDescriptor.className}:${testDescriptor.name}"
    }
}
