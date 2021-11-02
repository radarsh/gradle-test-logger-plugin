package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor

@CompileStatic
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
        logger.log theme.suiteStandardStreamText(suite, outputCollector.pop(suite), result)

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

            descriptor.ancestors.each { ancestor ->
                logger.log theme.suiteStandardStreamText(descriptor, outputCollector.pop(ancestor), result)
            }

            logger.log theme.testStandardStreamText(descriptor, outputCollector.pop(descriptor), result)
        }
    }

    @Override
    protected TestDescriptorWrapper wrap(TestDescriptor descriptor) {
        List<TestDescriptor> rawAncestors = []
        List<TestDescriptorWrapper> wrappedAncestors = []

        def current = descriptor.parent

        while (current && current.name && !isGradleSuite(current.name)) {
            rawAncestors << current
            current = current.parent
        }

        rawAncestors.reverse().withIndex().collect { TestDescriptor anc, int i ->
            wrappedAncestors << new TestDescriptorWrapper(anc, testLoggerExtension, wrappedAncestors.subList(0, i))
        }

        new TestDescriptorWrapper(descriptor, testLoggerExtension, wrappedAncestors)
    }
}
