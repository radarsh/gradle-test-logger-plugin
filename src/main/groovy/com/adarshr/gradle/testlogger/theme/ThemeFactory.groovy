package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension

import static com.adarshr.gradle.testlogger.theme.ThemeType.*

class ThemeFactory {

    static Theme getTheme(TestLoggerExtension extension) {
        switch (extension.theme) {
            case PLAIN:
                return new PlainTheme(extension)
            case STANDARD:
                return new StandardTheme(extension)
            case MOCHA:
                return new MochaTheme(extension)
            default:
                throw new IllegalArgumentException("Unsupported theme type ${extension.theme.name().toLowerCase()}")
        }
    }
}
