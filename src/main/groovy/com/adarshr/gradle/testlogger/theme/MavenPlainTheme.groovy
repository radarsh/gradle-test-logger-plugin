package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.MAVEN
import static com.adarshr.gradle.testlogger.theme.ThemeType.MAVEN_PLAIN
import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.util.RendererUtils.escape
import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@CompileStatic
@InheritConstructors
class MavenPlainTheme extends PlainTheme {

    ThemeType type = MAVEN_PLAIN

    @Override
    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        if (!extension.showSummary) {
            return ''
        }
        def line = new StringBuilder()

        line << "Summary:${lineSeparator()}${lineSeparator()}"

        line << " Tests run: ${result.testCount}, " +
            "Failures: ${result.failedTestCount}, Skipped: ${result.skippedTestCount}, " +
            "Time elapsed: ${result.duration}"
    }
}
