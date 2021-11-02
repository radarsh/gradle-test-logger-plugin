package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN_PARALLEL

@CompileStatic
@InheritConstructors
class PlainParallelTheme extends PlainTheme {

    ThemeType type = PLAIN_PARALLEL

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.testTextInternal("${descriptor.trail} ${descriptor.displayName} ${RESULT_TYPE_MAPPING[result.resultType]}", descriptor, result)
    }

    @Override
    protected String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result, int indent) {
        return super.exceptionText(descriptor, result, 2)
    }

    @Override
    protected String suiteStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        super.standardStreamTextInternal(lines, 2)
    }

    @Override
    protected String testStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        super.standardStreamTextInternal(lines, 2)
    }
}
