package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

@InheritConstructors
class PlainParallelTheme extends PlainTheme {

    @Override
    protected String suiteTextInternal(TestDescriptor descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptor descriptor, TestResult result) {
        super.testText("${escape(descriptor.className)} ${displayName(descriptor)} ${RESULT_TYPE_MAPPING[result.resultType]}", descriptor, result)
    }

    @Override
    protected String suiteStandardStreamTextInternal(String lines) {
        super.standardStreamText(lines, 2)
    }

    @Override
    protected String testStandardStreamTextInternal(String lines) {
        super.standardStreamText(lines, 2)
    }
}
