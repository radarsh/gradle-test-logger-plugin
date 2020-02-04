package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import groovy.transform.CompileStatic
import org.gradle.api.tasks.testing.Test

@CompileStatic
class TestLoggerWrapper implements TestLogger {

    @Delegate
    private final TestLogger testLoggerDelegate

    TestLoggerWrapper(Test test, TestLoggerExtension testLoggerExtension) {
        Theme theme = ThemeFactory.getTheme(test, testLoggerExtension)

        if (theme.type.parallel) {
            testLoggerDelegate = new ParallelTestLogger(test.project, testLoggerExtension, theme)
        } else {
            testLoggerDelegate = new SequentialTestLogger(test.project, testLoggerExtension, theme)
        }
    }
}
