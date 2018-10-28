package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.testng.TestNGOptions

class TestLoggerWrapper implements TestLogger {

    @Delegate
    private final TestLogger testLoggerDelegate

    TestLoggerWrapper(Project project, Test test, TestLoggerExtension testLoggerExtension) {
        Theme theme = ThemeFactory.getTheme(testLoggerExtension)

        ensureCorrectThemeType(test, theme)

        if (theme.type.parallel) {
            testLoggerDelegate = new ParallelTestLogger(project, testLoggerExtension)
        } else {
            testLoggerDelegate = new SequentialTestLogger(project, testLoggerExtension)
        }
    }

    private static void ensureCorrectThemeType(Test test, Theme theme) {
        boolean testNgParallelMode = test.options instanceof TestNGOptions && test.options.parallel

        if ((test.maxParallelForks > 1 || testNgParallelMode) && !theme.type.parallel) {
            throw new GradleException("Parallel execution is not supported for theme type '${theme.type.name}'. " +
                "Must be one of ${ThemeType.parallelThemeNames}")
        }
    }
}
