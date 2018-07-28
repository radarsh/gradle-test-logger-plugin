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
    private final Map<String, StringBuilder> groupedStandardStreamCollector = [:]
    private final List<String> suites

    ParallelTestLogger(Project project) {
        logger = new ConsoleLogger(project.logger)
        theme = ThemeFactory.getTheme(project.testlogger as TestLoggerExtension)
        suites = new ArrayList<>(100)
    }

    @Override
    void beforeSuite(TestDescriptor suite) {
        if (!suite.parent) {
            logger.logNewLine()
        }
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        logger.log theme.suiteStandardStreamText(groupedStandardStreamCollector.computeIfAbsent(getSuiteKey(suite), {
            new StringBuilder()
        }).toString())
        groupedStandardStreamCollector.remove(getSuiteKey(suite)).length = 0

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

        def suiteStandardStreamText = groupedStandardStreamCollector.computeIfAbsent(getSuiteKey(descriptor), {
            new StringBuilder()
        }).toString()
        def testStandardStreamText = groupedStandardStreamCollector.computeIfAbsent(getKey(descriptor), {
            new StringBuilder()
        }).toString()


        logger.log theme.suiteStandardStreamText(suiteStandardStreamText)
        logger.log theme.testStandardStreamText(testStandardStreamText)

        groupedStandardStreamCollector.remove(getSuiteKey(descriptor)).length = 0
        groupedStandardStreamCollector.remove(getKey(descriptor)).length = 0
    }

    @Override
    void onOutput(TestDescriptor descriptor, TestOutputEvent outputEvent) {
        groupedStandardStreamCollector.computeIfAbsent(getKey(descriptor), { new StringBuilder() }) << outputEvent.message
    }

    private static String getKey(TestDescriptor descriptor) {
        "${descriptor.className}:${descriptor.name}"
    }

    private static String getSuiteKey(TestDescriptor descriptor) {
        "${descriptor.className}:${descriptor.className}"
    }
}
