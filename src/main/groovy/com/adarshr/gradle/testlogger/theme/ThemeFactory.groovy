package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import org.gradle.api.GradleException

import static com.adarshr.gradle.testlogger.theme.ThemeType.*

class ThemeFactory {

    static Theme getTheme(TestLoggerExtension extension) {
        switch (extension.theme) {
            case PLAIN:
                return new PlainTheme(extension)
            case PLAIN_PARALLEL:
                return new PlainParallelTheme(extension)
            case STANDARD:
                return new StandardTheme(extension)
            case STANDARD_PARALLEL:
                return new StandardParallelTheme(extension)
            case MOCHA:
                return new MochaTheme(extension)
            case MOCHA_PARALLEL:
                return new MochaParallelTheme(extension)
            default:
                throw new GradleException("Unknown theme '${extension.theme}'. Must be one of ${allThemeNames}")
        }
    }
}
