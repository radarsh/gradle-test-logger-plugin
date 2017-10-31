package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

class TestEventLogger implements TestListener {

    private final Theme theme
    private final ConsoleLogger logger
    private boolean logBeforeSuite

    TestEventLogger(Project project) {
        logger = new ConsoleLogger(project.logger)
        theme = ThemeFactory.getTheme(project.testlogger as TestLoggerExtension)
    }

    @Override
    void beforeSuite(TestDescriptor suite) {
        if (!suite.parent) {
            logger.log ''
        }

        if (logBeforeSuite && suite.className) {
            logger.log theme.suiteText(suite)
        }
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        if (suite.className && result.testCount) {
            logger.log ''
            logBeforeSuite = false
        }

        if (!suite.parent) {
            logger.log theme.summaryText(suite, result)
        }
    }

    @Override
    void beforeTest(TestDescriptor descriptor) {
        if (!logBeforeSuite) {
            logBeforeSuite = true
            beforeSuite(descriptor)
        }
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
        logger.log theme.testText(descriptor, result)
    }
}
