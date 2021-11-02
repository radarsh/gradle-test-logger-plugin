package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

@InheritConstructors
@CompileStatic
class SequentialTestLogger extends TestLoggerAdapter {

    private final Map<String, Boolean> suites = [:]

    private String lastLogged = 'suite'
    private int lastDepth = 0

    @Override
    void beforeSuite(TestDescriptorWrapper suite) {
        suites << [(suite.id): false]
    }

    @Override
    void afterSuite(TestDescriptorWrapper suite, TestResultWrapper result) {
        logger.log theme.suiteStandardStreamText(suite, outputCollector.pop(suite), result)

        if (!suite.parent) {
            logger.logNewLine()
            logger.log theme.summaryText(suite, result)
        }

        suites.remove(suite.id)
    }

    @Override
    void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        descriptor.ancestors
            .findAll { ancestor -> !suites[ancestor.id] }
            .each { ancestor ->
                def suiteText = theme.suiteText(ancestor, result)

                if (suiteText) {
                    if (lastLogged == 'test') {
                        logger.logNewLine()
                    }

                    logger.log theme.suiteStandardStreamText(ancestor, outputCollector.pop(ancestor), result)
                    logger.log suiteText
                    lastLogged = 'suite'

                    suites[ancestor.id] = true
                }
            }

        def testText = theme.testText(descriptor, result)

        if (testText) {
            if (lastLogged == 'test' && lastDepth > descriptor.depth) {
                logger.logNewLine()
            }

            lastLogged = 'test'
            lastDepth = descriptor.depth
        }

        logger.log testText
        logger.log theme.testStandardStreamText(descriptor, outputCollector.pop(descriptor), result)
    }
}
