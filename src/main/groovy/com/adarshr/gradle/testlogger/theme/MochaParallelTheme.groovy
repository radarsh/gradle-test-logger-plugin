package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL

@CompileStatic
@InheritConstructors
class MochaParallelTheme extends MochaTheme {

    ThemeType type = MOCHA_PARALLEL

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.testTextInternal("  [erase-ahead,default]${descriptor.trail} ", descriptor, result)
    }

    @Override
    String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.exceptionText(descriptor, result, 4)
    }

    @Override
    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.summaryText(descriptor, result, 2)
    }

    @Override
    protected String suiteStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        super.standardStreamTextInternal(lines, 4)
    }

    @Override
    protected String testStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        super.standardStreamTextInternal(lines, 4)
    }
}
