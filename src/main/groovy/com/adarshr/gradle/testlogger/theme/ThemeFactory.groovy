package com.adarshr.gradle.testlogger.theme

import static com.adarshr.gradle.testlogger.theme.ThemeType.*

class ThemeFactory {

    static Theme loadTheme(ThemeType themeType) {
        switch (themeType) {
            case PLAIN:
                return new PlainTheme()
            case STANDARD:
                return new StandardTheme()
            case MOCHA:
                return new MochaTheme()
            default:
                throw new IllegalArgumentException("Unsupported theme type ${themeType.name().toLowerCase()}")
        }
    }
}
