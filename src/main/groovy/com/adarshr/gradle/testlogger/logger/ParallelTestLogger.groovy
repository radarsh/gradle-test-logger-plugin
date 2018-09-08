package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.InheritConstructors

@InheritConstructors
class ParallelTestLogger extends TestLoggerAdapter {

    @Override
    void beforeSuite(TestDescriptorWrapper suite) {
        if (!suite.parent) {
            logger.logNewLine()
        }
    }

    @Override
    void afterSuite(TestDescriptorWrapper suite, TestResultWrapper result) {
        logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(suite), result)

        if (!suite.parent) {
            logger.logNewLine()
            logger.log theme.summaryText(suite, result)
        }
    }

    @Override
    void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        def testText = theme.testText(descriptor, result)

        if (testText) {
            logger.log testText
            logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(descriptor), result)
            logger.log theme.testStandardStreamText(outputCollector.removeTestOutput(descriptor), result)
        }
    }
}
