package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.InheritConstructors

@InheritConstructors
class SequentialTestLogger extends TestLoggerAdapter {

    private final Map<String, Boolean> suites = [:]

    @Override
    void beforeSuite(TestDescriptorWrapper suite) {
        suites << [(suite.className): false]
    }

    @Override
    void afterSuite(TestDescriptorWrapper suite, TestResultWrapper result) {
        logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(suite), result)

        if (!suite.parent) {
            logger.logNewLine()
            logger.log theme.summaryText(suite, result)
        }

        suites.remove(suite.className)
    }

    @Override
    void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
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
