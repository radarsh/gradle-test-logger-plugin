package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD_PARALLEL

@CompileStatic
@InheritConstructors
class StandardParallelTheme extends StandardTheme {

    ThemeType type = STANDARD_PARALLEL

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.testTextInternal("[erase-ahead,bold]${descriptor.trail}[bold-off] ${descriptor.displayName}", descriptor, result)
    }

    @Override
    String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        super.exceptionText(descriptor, result, 2)
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
