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
        collector.computeIfAbsent(descriptor.id, mappingFunction) << output
    }

    String pop(TestDescriptorWrapper descriptor) {
        def output = collector.computeIfAbsent(descriptor.id, mappingFunction).toString()
        collector.remove(descriptor.id).length = 0
        output
    }
}
