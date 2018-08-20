package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

@InheritConstructors
class StandardParallelTheme extends StandardTheme {

    @Override
    protected String suiteTextInternal(TestDescriptor descriptor) {
        ''
    }

    @Override
    protected String testTextInternal(TestDescriptor descriptor, TestResult result) {
        super.testText("[erase-ahead,bold]${escape(descriptor.className)}[bold-off] ${displayName(descriptor)}", descriptor, result)
    }

    @Override
    String exceptionText(TestDescriptor descriptor, TestResult result) {
        super.exceptionText(descriptor, result, 2)
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
