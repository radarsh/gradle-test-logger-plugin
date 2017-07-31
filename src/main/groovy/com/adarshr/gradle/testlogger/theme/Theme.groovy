package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

interface Theme {

    String testCase(TestDescriptor descriptor)

    String beforeTest(TestDescriptor descriptor)

    String afterTest(TestDescriptor descriptor, TestResult result)
}
