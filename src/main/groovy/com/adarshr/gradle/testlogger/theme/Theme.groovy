package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper

interface Theme {

    ThemeType getType()

    String suiteText(TestDescriptorWrapper descriptor, TestResultWrapper result)

    String testText(TestDescriptorWrapper descriptor, TestResultWrapper result)

    String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result)

    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result)

    String suiteStandardStreamText(String lines, TestResultWrapper result)

    String testStandardStreamText(String lines, TestResultWrapper result)
}
