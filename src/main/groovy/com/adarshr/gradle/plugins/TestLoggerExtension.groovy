package com.adarshr.gradle.plugins

import com.adarshr.gradle.plugins.theme.ThemeType

import static com.adarshr.gradle.plugins.theme.ThemeType.STANDARD

class TestLoggerExtension {

    ThemeType theme = STANDARD

    void setTheme(String theme) {
        this.theme = ThemeType.valueOf(theme.toUpperCase())
    }
}
