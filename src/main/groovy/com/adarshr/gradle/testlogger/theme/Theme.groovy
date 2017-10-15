package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

interface Theme {

    String suiteText(TestDescriptor descriptor)

    String testText(TestDescriptor descriptor, TestResult result)

    String exceptionText(TestDescriptor descriptor, TestResult result)
}
