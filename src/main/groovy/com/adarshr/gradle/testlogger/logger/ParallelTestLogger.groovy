package com.adarshr.gradle.testlogger.logger


import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

@InheritConstructors
class ParallelTestLogger extends TestLoggerAdapter {

    @Override
    void beforeSuite(TestDescriptor suite) {
        if (!suite.parent) {
            logger.logNewLine()
        }
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(suite), result)

        if (!suite.parent) {
            logger.logNewLine()
            logger.log theme.summaryText(suite, result)
        }
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
        def testText = theme.testText(descriptor, result)

        if (testText) {
            logger.log testText
            logger.log theme.suiteStandardStreamText(outputCollector.removeSuiteOutput(descriptor), result)
            logger.log theme.testStandardStreamText(outputCollector.removeTestOutput(descriptor), result)
        }
    }
}
