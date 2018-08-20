package com.adarshr.gradle.testlogger.logger

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

@InheritConstructors
class SequentialTestLogger extends TestLoggerAdapter {

    private final Map<String, Boolean> suites = [:]

    @Override
    void beforeSuite(TestDescriptor suite) {
        suites << [(suite.className): false]
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(suite), result)

        if (!suite.parent) {
            logger.logNewLine()
            logger.log theme.summaryText(suite, result)
        }

        suites.remove(suite.className)
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
        if (!suites[descriptor.className]) {
            def suiteText = theme.suiteText(descriptor, result)

            if (suiteText) {
                logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(descriptor), result)
                logger.logNewLine()
                logger.log suiteText

                suites[descriptor.className] = true
            }
        }

        logger.log theme.testText(descriptor, result)
        logger.log theme.testStandardStreamText(outputCollector.removeTestOutput(descriptor), result)
    }
}
