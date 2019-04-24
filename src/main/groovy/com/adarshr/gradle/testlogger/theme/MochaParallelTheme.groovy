package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

@InheritConstructors
@CompileStatic
class MochaParallelTheme extends MochaTheme {

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.testTextInternal("  [erase-ahead,default]${descriptor.classDisplayName} ", descriptor, result)
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
    protected String suiteStandardStreamTextInternal(String lines) {
        super.standardStreamTextInternal(lines, 4)
    }

    @Override
    protected String testStandardStreamTextInternal(String lines) {
        super.standardStreamTextInternal(lines, 4)
    }
}
