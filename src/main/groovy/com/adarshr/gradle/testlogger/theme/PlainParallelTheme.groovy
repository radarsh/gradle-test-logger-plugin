package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

@InheritConstructors
class PlainParallelTheme extends PlainTheme {

    @Override
    String suiteText(TestDescriptor descriptor) {
        ''
    }

    @Override
    String testText(TestDescriptor descriptor, TestResult result) {
        super.testText("${escape(descriptor.className)} ${displayName(descriptor)} ${RESULT_TYPE_MAPPING[result.resultType]}", descriptor, result)
    }

    @Override
    String suiteStandardStreamText(String lines) {
        super.standardStreamText(lines, 2)
    }

    @Override
    String testStandardStreamText(String lines) {
        super.standardStreamText(lines, 2)
    }
}
