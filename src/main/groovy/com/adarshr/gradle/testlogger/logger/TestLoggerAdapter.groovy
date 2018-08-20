package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestOutputEvent
import org.gradle.api.tasks.testing.TestResult


class TestLoggerAdapter implements TestLogger {

    protected final Theme theme
    protected final ConsoleLogger logger
    protected final OutputCollector outputCollector

    TestLoggerAdapter(Project project) {
        logger = new ConsoleLogger(project.logger)
        theme = ThemeFactory.getTheme(project.testlogger as TestLoggerExtension)
        outputCollector = new OutputCollector()
    }

    @Override
    void beforeSuite(TestDescriptor descriptor) {
    }

    @Override
    void afterSuite(TestDescriptor descriptor, TestResult result) {
    }

    @Override
    void beforeTest(TestDescriptor descriptor) {
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
    }

    @Override
    void onOutput(TestDescriptor descriptor, TestOutputEvent outputEvent) {
        outputCollector.collect(descriptor, outputEvent.message)
    }
}
