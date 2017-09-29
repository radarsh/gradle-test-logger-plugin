package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType

import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD

class TestLoggerExtension {

    ThemeType theme = STANDARD

    void setTheme(String theme) {
        this.theme = ThemeType.valueOf(theme.toUpperCase())
    }
}
