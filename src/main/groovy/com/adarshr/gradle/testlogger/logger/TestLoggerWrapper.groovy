package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import groovy.transform.CompileStatic
import org.gradle.StartParameter
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.testing.Test

@CompileStatic
class TestLoggerWrapper implements TestLogger {

    private Provider<TestLoggerAdapter> testLoggerDelegate
    private TestLogger cachedDelegate

    TestLoggerWrapper(StartParameter startParameter, Test test, TestLoggerExtension testLoggerExtension) {
        this.testLoggerDelegate = test.project.provider {
            Theme theme = ThemeFactory.getTheme(startParameter, test, testLoggerExtension)

            if (theme.type.parallel) {
                return new ParallelTestLogger(test.logger, testLoggerExtension, theme)
            } else {
                return new SequentialTestLogger(test.logger, testLoggerExtension, theme)
            }
        }
    }

    @Delegate
    TestLogger getTestLoggerDelegate() {
        if (cachedDelegate == null) {
            cachedDelegate = testLoggerDelegate.get()
        }
        return cachedDelegate
    }
}
