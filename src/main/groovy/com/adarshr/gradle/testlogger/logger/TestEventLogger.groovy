package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import org.gradle.api.Project
import org.gradle.api.tasks.testing.*

class TestEventLogger implements TestListener, TestOutputListener {

    private final Theme theme
    private final ConsoleLogger logger
    private boolean logBeforeSuite
    private final StringBuilder standardStreamCollector

    TestEventLogger(Project project) {
        logger = new ConsoleLogger(project.logger)
        theme = ThemeFactory.getTheme(project.testlogger as TestLoggerExtension)
        standardStreamCollector = new StringBuilder()
    }

    @Override
    void beforeSuite(TestDescriptor suite) {
        logger.log theme.suiteStandardStreamText(standardStreamCollector.toString())
        standardStreamCollector.length = 0

        if (!suite.parent) {
            logger.logNewLine()
        }

        if (logBeforeSuite && suite.className) {
            logger.log theme.suiteText(suite)
        }
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        logger.log theme.suiteStandardStreamText(standardStreamCollector.toString())
        standardStreamCollector.length = 0

        if (suite.className && result.testCount) {
            logger.logNewLine()
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
        logger.log theme.testStandardStreamText(standardStreamCollector.toString())

        standardStreamCollector.length = 0
    }

    @Override
    void onOutput(TestDescriptor testDescriptor, TestOutputEvent outputEvent) {
        standardStreamCollector << outputEvent.message
    }
}
