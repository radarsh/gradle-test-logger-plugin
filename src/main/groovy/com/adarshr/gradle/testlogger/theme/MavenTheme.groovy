package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.MAVEN
import static java.lang.System.lineSeparator

@CompileStatic
@InheritConstructors
class MavenTheme extends StandardTheme {

    ThemeType type = MAVEN

    @Override
    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        if (!extension.showSummary) {
            return ''
        }

        String colour = 'green';

        if (result.failedTestCount > 0) {
            colour = 'red';
        } else if (result.skippedTestCount > 0) {
            colour = 'yellow';
        }

        def line = new StringBuilder()

        line << "[erase-ahead,default]Summary:${lineSeparator()}${lineSeparator()}"

        line << "[bold,${colour}] Tests run: ${result.testCount}, " +
            "Failures: ${result.failedTestCount}, Skipped: ${result.skippedTestCount}, " +
            "Time elapsed: ${result.duration}[/]"
    }
}
