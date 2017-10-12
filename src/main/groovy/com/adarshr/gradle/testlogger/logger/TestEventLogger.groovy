package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.theme.Theme
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestListener
import org.gradle.api.tasks.testing.TestResult

import static com.adarshr.gradle.testlogger.theme.ThemeFactory.loadTheme
import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

class TestEventLogger implements TestListener {

    private final boolean plainConsole
    private final Theme theme
    private final ConsoleLogger logger
    private boolean logBeforeSuite

    TestEventLogger(Project project) {
        logger = new ConsoleLogger(project.logger)
        plainConsole = project.gradle.startParameter.consoleOutput == Plain || project.testlogger.theme == PLAIN
        theme = plainConsole ? loadTheme(PLAIN) : loadTheme(project.testlogger.theme)
    }

    @Override
    void beforeSuite(TestDescriptor suite) {
        if (!suite.parent) {
            logger.log ''
        }

        if (logBeforeSuite && suite.className) {
            logger.log theme.beforeSuite(suite)
        }
    }

    @Override
    void afterSuite(TestDescriptor suite, TestResult result) {
        if (suite.className && result.testCount) {
            logger.log ''
            logBeforeSuite = false
        }
    }

    @Override
    void beforeTest(TestDescriptor descriptor) {
        if (!logBeforeSuite) {
            logBeforeSuite = true
            beforeSuite(descriptor)
        }
    }

    @Override
    void afterTest(TestDescriptor descriptor, TestResult result) {
        logger.log theme.afterTest(descriptor, result)
    }
}
