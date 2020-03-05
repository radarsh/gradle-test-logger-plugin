package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import groovy.transform.CompileStatic
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.testng.TestNGOptions

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.theme.ThemeType.fromName
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

@CompileStatic
class ThemeFactory {

    static Theme getTheme(Test test, TestLoggerExtension extension) {
        resolveThemeType(test, extension).themeClass.newInstance(extension)
    }

    private static ThemeType resolveThemeType(Test test, TestLoggerExtension extension) {
        ThemeType themeType = extension.theme

        if (test.project.gradle.startParameter.consoleOutput == Plain) {
            themeType = PLAIN
        }

        if (isParallelMode(test) && !themeType.parallel) {
            themeType = fromName(themeType.parallelFallback)
        }

        if (themeType != extension.theme) {
            test.project.logger.info("Test logger theme for task ${test.name} overridden " +
                "from ${extension.theme.name} to ${themeType.name}")
        }

        themeType
    }

    private static boolean isParallelMode(Test test) {
        boolean testNgParallelMode = test.options instanceof TestNGOptions && (test.options as TestNGOptions).parallel

        test.maxParallelForks > 1 || testNgParallelMode
    }
}
