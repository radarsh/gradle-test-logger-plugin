package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

interface Theme {

    String beforeSuite(TestDescriptor descriptor)

    String afterTest(TestDescriptor descriptor, TestResult result)
}
