package com.adarshr.gradle.testlogger.theme


import spock.lang.Unroll

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class StandardThemeSpec extends BaseThemeSpec {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final int LINE_NUMBER = exception.stackTrace.find { it.className == owner.name }.lineNumber

    Theme theme

    def setup() {
        theme = new StandardTheme(testLoggerExtensionMock)
    }

    def "suite text"() {
        given:
            testDescriptorMock.className >> 'ClassName'
        when:
            def actual = theme.suiteText(testDescriptorMock, testResultMock)
        then:
            actual == "[erase-ahead,bold]ClassName[/]${lineSeparator()}"
    }

    @Unroll
    def "after test with result type #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testDescriptorMock.displayName >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            resultType | expected
            SUCCESS    | '[erase-ahead,bold]  Test [bold-off]test name[green] PASSED[/]'
            FAILURE    | '[erase-ahead,bold]  Test [bold-off]test name[red] FAILED[/]'
            SKIPPED    | '[erase-ahead,bold]  Test [bold-off]test name[yellow] SKIPPED[/]'
    }

    def "after test with result type failure and showExceptions true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            theme = new StandardTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.displayName >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual ==
                """|[erase-ahead,bold]  Test [bold-off]floppy test[red] FAILED[red]
                   |
                   |  java.lang.AssertionError: This is wrong
                   |      at com.adarshr.gradle.testlogger.theme.StandardThemeSpec.getException(StandardThemeSpec.groovy:${LINE_NUMBER})
                   |[/]""".stripMargin().replace('\n', lineSeparator())
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            theme = new StandardTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.displayName >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        expect:
            theme.exceptionText(testDescriptorMock, testResultMock) ==
                """|[red]
                   |
                   |  java.lang.AssertionError: This is wrong
                   |      at com.adarshr.gradle.testlogger.theme.StandardThemeSpec.getException(StandardThemeSpec.groovy:${LINE_NUMBER})
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "exception text when showExceptions is false"() {
        given:
            testLoggerExtensionMock.showExceptions >> false
            testResultMock.resultType >> FAILURE
            testDescriptorMock.displayName >> 'floppy test'
        expect:
            !theme.exceptionText(testDescriptorMock, testResultMock)
    }

    @Unroll
    def "show duration if slowThreshold is exceeded for resultType #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testResultMock.duration >> '10s'
            testResultMock.tooSlow >> true
            testDescriptorMock.displayName >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == text
        where:
            resultType | text
            SUCCESS    | '[erase-ahead,bold]  Test [bold-off]test name[green] PASSED[red] (10s)[/]'
            FAILURE    | '[erase-ahead,bold]  Test [bold-off]test name[red] FAILED[red] (10s)[/]'
    }

    @Unroll
    def "show duration if slowThreshold is approaching for resultType #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testResultMock.duration >> '1.5s'
            testResultMock.mediumSlow >> true
            testDescriptorMock.displayName >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == text
        where:
            resultType | text
            SUCCESS    | '[erase-ahead,bold]  Test [bold-off]test name[green] PASSED[yellow] (1.5s)[/]'
            FAILURE    | '[erase-ahead,bold]  Test [bold-off]test name[red] FAILED[yellow] (1.5s)[/]'
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
            theme = new StandardTheme(testLoggerExtensionMock)
        when:
            def actual = theme.summaryText(testDescriptorMock, testResultMock)
        then:
            actual == summaryText
        where:
            summaryText                                                                                                   | success | failure | skipped
            "[erase-ahead,bold,green]SUCCESS: [default]Executed 10 tests in 10s[/]${lineSeparator()}"                     | 10      | 0       | 0
            "[erase-ahead,bold,green]SUCCESS: [default]Executed 7 tests in 10s (2 skipped)[/]${lineSeparator()}"          | 5       | 0       | 2
            "[erase-ahead,bold,red]FAILURE: [default]Executed 8 tests in 10s (3 failed)[/]${lineSeparator()}"             | 5       | 3       | 0
            "[erase-ahead,bold,red]FAILURE: [default]Executed 10 tests in 10s (3 failed, 2 skipped)[/]${lineSeparator()}" | 5       | 3       | 2
    }

    def "summary when showSummary is false"() {
        expect:
            !theme.summaryText(testDescriptorMock, testResultMock)
    }

    def "standard stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new StandardTheme(testLoggerExtensionMock)
        expect:
            theme.testStandardStreamText(streamLines, testResultMock) ==
                '''|[default]
                   |    Hello
                   |    World [brackets] \u001B\\[0mANSI[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "standard stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new StandardTheme(testLoggerExtensionMock)
        expect:
            !theme.testStandardStreamText(streamLines, testResultMock)
    }

    def "suite stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new StandardTheme(testLoggerExtensionMock)
        expect:
            theme.suiteStandardStreamText(streamLines, testResultMock) ==
                '''|[default]
                   |  Hello
                   |  World [brackets] \u001B\\[0mANSI[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "suite stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new StandardTheme(testLoggerExtensionMock)
        expect:
            !theme.suiteStandardStreamText(streamLines, testResultMock)
    }
}
