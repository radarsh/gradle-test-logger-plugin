package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.Theme
import com.adarshr.gradle.testlogger.theme.ThemeFactory
import org.gradle.api.Project
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

class TestLoggerTestListener implements TestListener {

    boolean plainConsole
    Theme theme
    Logger logger
    Set classes

    TestLoggerTestListener(Project project) {
        classes = []
        logger = project.logger
        plainConsole = project.gradle.startParameter.consoleOutput == Plain || project.testlogger.theme == PLAIN


        if (plainConsole) {
            theme = ThemeFactory.loadTheme(PLAIN)
        } else {
            theme = ThemeFactory.loadTheme(project.testlogger.theme)
        }
    }

    @Override
    void beforeSuite(TestDescriptor suite) {

    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {

    }

    @Override
    void beforeTest(TestDescriptor descriptor) {
        if (!classes.contains(descriptor.className)) {
            classes << descriptor.className
            logger.lifecycle theme.testCase(descriptor)
        }

        if (!plainConsole) {
            logger.lifecycle theme.beforeTest(descriptor)
        }
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
        logger.lifecycle theme.afterTest(descriptor, result)
    }
}
