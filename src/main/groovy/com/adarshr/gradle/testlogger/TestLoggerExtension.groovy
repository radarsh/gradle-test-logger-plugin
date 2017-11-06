package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.Project
import org.gradle.api.logging.configuration.ConsoleOutput

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

class TestLoggerExtension {

    ThemeType theme = STANDARD
    boolean showExceptions = true
    long slowThreshold = 2000
    boolean showSummary = true

    private final ConsoleOutput consoleType

    TestLoggerExtension(Project project) {
        this.consoleType = project.gradle.startParameter.consoleOutput
        this.theme = project.gradle.startParameter.consoleOutput == Plain ? PLAIN : this.theme
    }

    void setTheme(String theme) {
        if (consoleType == Plain) {
            return
        }

        this.theme = ThemeType.valueOf(theme.toUpperCase())
    }

    void setTheme(ThemeType theme) {
        if (consoleType == Plain) {
            return
        }

        this.theme = theme
    }
}
