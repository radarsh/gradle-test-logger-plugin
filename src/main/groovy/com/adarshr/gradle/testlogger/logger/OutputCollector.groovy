package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import groovy.transform.CompileStatic

@CompileStatic
class OutputCollector {

    private static final Closure mappingFunction = {
        new StringBuilder()
    }

    private final Map<String, StringBuilder> collector = [:]

    void collect(TestDescriptorWrapper descriptor, String output) {
        collector.computeIfAbsent(descriptor.testKey, mappingFunction) << output
    }

    String removeSuiteOutput(TestDescriptorWrapper descriptor) {
        def output = collector.computeIfAbsent(descriptor.suiteKey, mappingFunction).toString()
        collector.remove(descriptor.suiteKey).length = 0
        output
    }

    String removeTestOutput(TestDescriptorWrapper descriptor) {
        def output = collector.computeIfAbsent(descriptor.testKey, mappingFunction).toString()
        collector.remove(descriptor.testKey).length = 0
        output
    }
}
