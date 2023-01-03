package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import groovy.transform.CompileStatic
import org.gradle.StartParameter
import org.gradle.api.tasks.testing.AbstractTestTask

@CompileStatic
class TestLoggerWrapper implements TestLogger {

    private final StartParameter startParameter
    private final AbstractTestTask test
    private final TestLoggerExtension testLoggerExtension

    private TestLogger testLoggerDelegate

    TestLoggerWrapper(StartParameter startParameter, AbstractTestTask test, TestLoggerExtension testLoggerExtension) {
        this.startParameter = startParameter
        this.test = test
        this.testLoggerExtension = testLoggerExtension
    }

    @Delegate
    TestLogger getTestLoggerDelegate() {
        if (testLoggerDelegate) {
            return testLoggerDelegate
        }

        Theme theme = ThemeFactory.getTheme(startParameter, test, testLoggerExtension)

        if (theme.type.parallel) {
            testLoggerDelegate = new ParallelTestLogger(test.logger, testLoggerExtension, theme)
        } else {
            testLoggerDelegate = new SequentialTestLogger(test.logger, testLoggerExtension, theme)
        }

        return testLoggerDelegate
    }
}
