package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

@InheritConstructors
@CompileStatic
class StandardParallelTheme extends StandardTheme {

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.testTextInternal("[erase-ahead,bold]${descriptor.classDisplayName}[bold-off] ${descriptor.displayName}", descriptor, result)
    }

    @Override
    String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.exceptionText(descriptor, result, 2)
    }

    @Override
    protected String suiteStandardStreamTextInternal(String lines) {
        super.standardStreamTextInternal(lines, 2)
    }

    @Override
    protected String testStandardStreamTextInternal(String lines) {
        super.standardStreamTextInternal(lines, 2)
    }
}
