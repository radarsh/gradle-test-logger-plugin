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
        def className = testDescriptor.className
        def classDisplayName = testDescriptor.properties.classDisplayName as String
        def useClassDisplayName = classDisplayName && classDisplayName != className && !className.endsWith(classDisplayName)

        if (testLoggerExtension.showSimpleNames) {
            className = className.substring(className.lastIndexOf('.') + 1)
            className = className.substring(className.lastIndexOf('$') + 1)
        }

        escape(useClassDisplayName ? classDisplayName : className)
    }

    @CompileDynamic
    String getDisplayName() {
        escape(testDescriptor.properties.displayName ?: testDescriptor.name as String)
    }

    String getSuiteKey() {
        "${testDescriptor.className}:${testDescriptor.className}"
    }

    String getTestKey() {
        "${testDescriptor.className}:${testDescriptor.name}"
    }
}
