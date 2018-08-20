package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

interface Theme {

    ThemeType getType()

    String suiteText(TestDescriptor descriptor, TestResult result)

    String testText(TestDescriptor descriptor, TestResult result)

    String exceptionText(TestDescriptor descriptor, TestResult result)

    String summaryText(TestDescriptor descriptor, TestResult result)

    String suiteStandardStreamText(String lines, TestResult result)

    String testStandardStreamText(String lines, TestResult result)
}
