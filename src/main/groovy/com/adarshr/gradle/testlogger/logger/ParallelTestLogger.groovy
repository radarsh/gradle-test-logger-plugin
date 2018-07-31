package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestOutputEvent
import org.gradle.api.tasks.testing.TestResult


class ParallelTestLogger implements TestLogger {

    private final Theme theme
    private final ConsoleLogger logger
    private boolean logBeforeSuite
    private final OutputCollector outputCollector
    private final List<String> suites

    ParallelTestLogger(Project project) {
        logger = new ConsoleLogger(project.logger)
        theme = ThemeFactory.getTheme(project.testlogger as TestLoggerExtension)
        suites = new ArrayList<>(100)
        outputCollector = new OutputCollector()
    }

    @Override
    void beforeSuite(TestDescriptor suite) {
        if (!suite.parent) {
            logger.logNewLine()
        }
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(suite))

        if (suite.className && result.testCount) {
            logBeforeSuite = false
        }

        if (!suite.parent) {
            logger.logNewLine()
            logger.log theme.summaryText(suite, result)
        }
    }

    @Override
    void beforeTest(TestDescriptor descriptor) {
        if (!suites.contains(descriptor.className)) {
            suites << descriptor.className
            beforeSuite(descriptor)
        }

        logBeforeSuite = true
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
        logger.log theme.testText(descriptor, result)
        logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(descriptor))
        logger.log theme.testStandardStreamText(outputCollector.removeTestOutput(descriptor))
    }

    @Override
    void onOutput(TestDescriptor descriptor, TestOutputEvent outputEvent) {
        outputCollector.collect(descriptor, outputEvent.message)
    }
}
