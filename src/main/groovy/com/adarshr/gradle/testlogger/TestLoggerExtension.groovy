package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.Project

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

class TestLoggerExtension {

    ThemeType theme = STANDARD

    /**
     * @see org.gradle.api.tasks.testing.logging.TestLogging#getShowExceptions()
     */
    boolean showExceptions = true

    long slowThreshold = 2000

    TestLoggerExtension(Project project) {
        this.theme = project.gradle.startParameter.consoleOutput == Plain ? PLAIN : this.theme
    }

    void setTheme(String theme) {
        this.theme = ThemeType.valueOf(theme.toUpperCase())
    }

    void setTheme(ThemeType theme) {
        this.theme = theme
    }
}
