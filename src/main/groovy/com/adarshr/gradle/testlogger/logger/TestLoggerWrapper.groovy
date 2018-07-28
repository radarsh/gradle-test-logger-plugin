package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.GradleException
import org.gradle.api.Project

class TestLoggerWrapper implements TestLogger {

    @Delegate
    private final TestLogger testLoggerDelegate

    TestLoggerWrapper(Project project, int maxParallelForks) {
        Theme theme = ThemeFactory.getTheme(project.testlogger as TestLoggerExtension)

        ensureCorrectThemeType(maxParallelForks, theme)

        if (theme.type.parallel) {
            testLoggerDelegate = new ParallelTestLogger(project)
        } else {
            testLoggerDelegate = new SequentialTestLogger(project)
        }
    }

    private static void ensureCorrectThemeType(int maxParallelForks, Theme theme) {
        if (maxParallelForks > 1 && !theme.type.parallel) {
            throw new GradleException("Parallel execution is not supported for theme type '${theme.type.name}'. " +
                "Must be one of ${ThemeType.parallelThemeNames}")
        }
    }
}
