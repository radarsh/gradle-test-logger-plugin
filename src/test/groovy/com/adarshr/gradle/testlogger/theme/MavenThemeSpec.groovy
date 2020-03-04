package com.adarshr.gradle.testlogger.theme

import spock.lang.Unroll

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.FAILURE
import static org.gradle.api.tasks.testing.TestResult.ResultType.SUCCESS

class MavenThemeSpec extends BaseThemeSpec {

    private static final String SUMMARY_LABEL = "[erase-ahead,default]Summary:${lineSeparator()}${lineSeparator()}"
    
    Theme theme

    def setup() {
        theme = new MavenTheme(testLoggerExtensionMock)
    }

    @Unroll
    def "summary text given #success success, #failure failed and #skipped skipped tests"() {
        given:
            testLoggerExtensionMock.showSummary >> true
            testResultMock.successfulTestCount >> success
            testResultMock.failedTestCount >> failure
            testResultMock.skippedTestCount >> skipped
            testResultMock.testCount >> success + failure + skipped
            testResultMock.duration >> '10s'
            testResultMock.resultType >> (failure ? FAILURE : SUCCESS) // what Gradle would do
        and:
            theme = new MavenTheme(testLoggerExtensionMock)
        when:
            def actual = theme.summaryText(testDescriptorMock, testResultMock)
        then:
            actual == summaryText
        where:
            summaryText                                                                                 | success | failure | skipped
            "${SUMMARY_LABEL}[bold,green] Tests run: 10, Failures: 0, Skipped: 0, Time elapsed: 10s[/]" | 10      | 0       | 0
            "${SUMMARY_LABEL}[bold,yellow] Tests run: 7, Failures: 0, Skipped: 2, Time elapsed: 10s[/]" | 5       | 0       | 2
            "${SUMMARY_LABEL}[bold,red] Tests run: 8, Failures: 3, Skipped: 0, Time elapsed: 10s[/]"    | 5       | 3       | 0
            "${SUMMARY_LABEL}[bold,red] Tests run: 10, Failures: 3, Skipped: 2, Time elapsed: 10s[/]"   | 5       | 3       | 2
    }

    def "summary when showSummary is false"() {
        expect:
            !theme.summaryText(testDescriptorMock, testResultMock)
    }
}
