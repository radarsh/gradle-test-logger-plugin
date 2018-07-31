package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

interface Theme {

    ThemeType getType()

    String suiteText(TestDescriptor descriptor)

    String testText(TestDescriptor descriptor, TestResult result)

    String exceptionText(TestDescriptor descriptor, TestResult result)

    String summaryText(TestDescriptor descriptor, TestResult result)

    String suiteStandardStreamText(String lines)

    String testStandardStreamText(String lines)
}
