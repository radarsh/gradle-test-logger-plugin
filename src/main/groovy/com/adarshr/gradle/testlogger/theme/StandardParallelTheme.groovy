package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

@InheritConstructors
class StandardParallelTheme extends StandardTheme {

    @Override
    String suiteText(TestDescriptor descriptor) {
        ''
    }

    @Override
    String testText(TestDescriptor descriptor, TestResult result) {
        super.testText("[erase-ahead,bold]${escape(descriptor.className)}[bold-off] ${displayName(descriptor)}", descriptor, result)
    }

    @Override
    String exceptionText(TestDescriptor descriptor, TestResult result) {
        super.exceptionText(descriptor, result, 2)
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
