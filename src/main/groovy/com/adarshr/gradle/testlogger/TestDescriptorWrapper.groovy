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

    final List<TestDescriptorWrapper> ancestors
    final int depth
    final String trail

    TestDescriptorWrapper(TestDescriptor testDescriptor, TestLoggerExtension testLoggerExtension, List<TestDescriptorWrapper> ancestors) {
        this.testDescriptor = testDescriptor
        this.testLoggerExtension = testLoggerExtension
        this.depth = ancestors.size()
        this.ancestors = ancestors
        this.trail = ancestors.collect { it.displayName }.join(' > ')
    }

    @CompileDynamic
    String getId() {
        testDescriptor.properties.id
    }

    String getDisplayName() {
        if (!depth && className && className.endsWith(testDescriptor.displayName)) {
            if (testLoggerExtension.showSimpleNames) {
                return escape(simpleClassName)
            }

            return escape(testDescriptor.className)
        }

        escape(testDescriptor.displayName)
    }

    private String getSimpleClassName() {
        def clazzName = testDescriptor.className

        clazzName = clazzName.substring(clazzName.lastIndexOf('.') + 1)
        clazzName = clazzName.substring(clazzName.lastIndexOf('$') + 1)

        clazzName
    }

    String getTrail() {
        trail
    }

    int getDepth() {
        depth
    }
}
