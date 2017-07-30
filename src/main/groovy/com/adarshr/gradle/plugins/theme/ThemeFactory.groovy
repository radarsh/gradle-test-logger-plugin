package com.adarshr.gradle.plugins.theme

import static com.adarshr.gradle.plugins.theme.ThemeType.MOCHA
import static com.adarshr.gradle.plugins.theme.ThemeType.STANDARD


class ThemeFactory {

    static Theme loadTheme(ThemeType themeType) {
        switch (themeType) {
            case STANDARD:
                return new StandardTheme()
            case MOCHA:
                return new MochaTheme()
            default:
                throw new IllegalArgumentException("Unsupported theme type ${themeType.name().toLowerCase()}")
        }
    }
}
