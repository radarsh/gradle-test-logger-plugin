package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import groovy.transform.CompileStatic
import org.gradle.StartParameter
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.testng.TestNGOptions

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.theme.ThemeType.fromName
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

@CompileStatic
class ThemeFactory {

    static Theme getTheme(StartParameter startParameter, Test test, TestLoggerExtension extension) {
        resolveThemeType(startParameter, test, extension).themeClass.newInstance(extension)
    }

    private static ThemeType resolveThemeType(StartParameter startParameter, Test test, TestLoggerExtension extension) {
        ThemeType themeType = extension.theme

        if (startParameter.consoleOutput == Plain) {
            themeType = PLAIN
        }

        if (isParallelMode(test) && !themeType.parallel) {
            themeType = fromName(themeType.parallelFallback)
        }

        if (themeType != extension.theme) {
            test.logger.info("Test logger theme for task ${test.name} overridden " +
                "from ${extension.theme.name} to ${themeType.name}")
        }

        themeType
    }

    private static boolean isParallelMode(Test test) {
        boolean testNgParallelMode = test.options instanceof TestNGOptions && (test.options as TestNGOptions).parallel

        test.maxParallelForks > 1 || testNgParallelMode
    }
}
