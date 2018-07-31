package com.adarshr.gradle.testlogger.logger

import groovy.transform.CompileStatic
import org.gradle.api.tasks.testing.TestDescriptor

@CompileStatic
class OutputCollector {

    private static final Closure mappingFunction = {
        new StringBuilder()
    }

    private final Map<String, StringBuilder> collector = [:]

    void collect(TestDescriptor descriptor, String output) {
        collector.computeIfAbsent(getTestKey(descriptor), mappingFunction) << output
    }

    String removeSuiteOutput(TestDescriptor descriptor) {
        def output = collector.computeIfAbsent(getSuiteKey(descriptor), mappingFunction).toString()
        collector.remove(getSuiteKey(descriptor)).length = 0
        output
    }

    String removeTestOutput(TestDescriptor descriptor) {
        def output = collector.computeIfAbsent(getTestKey(descriptor), mappingFunction).toString()
        collector.remove(getTestKey(descriptor)).length = 0
        output
    }

    private static String getTestKey(TestDescriptor descriptor) {
        "${descriptor.className}:${descriptor.name}"
    }

    private static String getSuiteKey(TestDescriptor descriptor) {
        "${descriptor.className}:${descriptor.className}"
    }
}
