package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.renderer.AnsiConsoleRenderer
import com.adarshr.gradle.testlogger.renderer.ConsoleRenderer
import com.adarshr.gradle.testlogger.theme.Theme
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

import static com.adarshr.gradle.testlogger.theme.ThemeFactory.loadTheme
import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

class TestLoggerTestListener implements TestListener {

    private final boolean plainConsole
    private final Theme theme
    private final Logger logger
    private final ConsoleRenderer renderer
    private boolean firstSuite = true

    TestLoggerTestListener(Project project) {
        logger = project.logger
        renderer = new AnsiConsoleRenderer()
        plainConsole = project.gradle.startParameter.consoleOutput == Plain || project.testlogger.theme == PLAIN
        theme = plainConsole ? loadTheme(PLAIN) : loadTheme(project.testlogger.theme)
    }

    @Override
    void beforeSuite(TestDescriptor suite) {
        if (firstSuite) {
            logger.lifecycle('')
            firstSuite = false
        }

        if (suite.className) {
            logger.lifecycle renderer.render(theme.beforeSuite(suite))
        }
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        if (suite.className) {
            logger.lifecycle('')
        }
    }

    @Override
    void beforeTest(TestDescriptor descriptor) {
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
        logger.lifecycle renderer.render(theme.afterTest(descriptor, result))
    }
}
