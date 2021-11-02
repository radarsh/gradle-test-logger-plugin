package com.adarshr.gradle.testlogger.theme


import spock.lang.Unroll

import static java.lang.System.lineSeparator
import static java.lang.System.setOut
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class PlainParallelThemeSpec extends BaseThemeSpec {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final int LINE_NUMBER = exception.stackTrace.find { it.className == owner.name }.lineNumber

    Theme theme

    def setup() {
        theme = new PlainParallelTheme(testLoggerExtensionMock)
    }

    def "before suite"() {
        expect:
            !theme.suiteText(testDescriptorMock, testResultMock)
    }

    @Unroll
    def "after test with result type #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testDescriptorMock.trail >> 'ClassName'
            testDescriptorMock.displayName >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            resultType | expected
            SUCCESS    | 'ClassName test name PASSED'
            FAILURE    | 'ClassName test name FAILED'
            SKIPPED    | 'ClassName test name SKIPPED'
    }

    def "after test with result type failure and showExceptions true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showCauses >> true
            theme = new PlainParallelTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.displayName >> 'floppy test'
            testDescriptorMock.className >> this.class.name
            testDescriptorMock.trail >> this.class.name
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual ==
                """|com.adarshr.gradle.testlogger.theme.PlainParallelThemeSpec floppy test FAILED
                   |
                   |  java.lang.AssertionError: This is wrong
                   |      at com.adarshr.gradle.testlogger.theme.PlainParallelThemeSpec.getException(PlainParallelThemeSpec.groovy:${LINE_NUMBER})
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showCauses >> true
            theme = new PlainParallelTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.displayName >> 'floppy test'
            testDescriptorMock.trail >> this.class.name
            testDescriptorMock.className >> this.class.name
        expect:
            theme.exceptionText(testDescriptorMock, testResultMock) ==
                """|
                   |
                   |  java.lang.AssertionError: This is wrong
                   |      at com.adarshr.gradle.testlogger.theme.PlainParallelThemeSpec.getException(PlainParallelThemeSpec.groovy:${LINE_NUMBER})
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


    def "exception text when showExceptions is true but exception is null"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> null
            testDescriptorMock.displayName >> 'exception is null test'
        expect:
            !theme.exceptionText(testDescriptorMock, testResultMock)
    }

    def "show duration if slowThreshold is exceeded"() {
        given:
            testResultMock.tooSlow >> true
            testResultMock.duration >> '10s'
            testResultMock.resultType >> SUCCESS
            testDescriptorMock.trail >> 'ClassName'
            testDescriptorMock.displayName >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == 'ClassName test name PASSED (10s)'
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
            theme = new PlainParallelTheme(testLoggerExtensionMock)
        when:
            def actual = theme.summaryText(testDescriptorMock, testResultMock)
        then:
            actual == summaryText
        where:
            summaryText                                                                 | success | failure | skipped
            "SUCCESS: Executed 10 tests in 10s${lineSeparator()}"                       | 10      | 0       | 0
            "SUCCESS: Executed 7 tests in 10s (2 skipped)${lineSeparator()}"            | 5       | 0       | 2
            "FAILURE: Executed 8 tests in 10s (3 failed)${lineSeparator()}"             | 5       | 3       | 0
            "FAILURE: Executed 10 tests in 10s (3 failed, 2 skipped)${lineSeparator()}" | 5       | 3       | 2
    }

    def "summary when showSummary is false"() {
        expect:
            !theme.summaryText(testDescriptorMock, testResultMock)
    }

    def "standard stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new PlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            theme.testStandardStreamText(testDescriptorMock, streamLines, testResultMock) ==
                '''|
                   |  Hello
                   |  World \\[brackets\\] \u001B\\[0mANSI
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "standard stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new PlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            !theme.testStandardStreamText(testDescriptorMock, streamLines, testResultMock)
    }

    def "suite stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new PlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            theme.suiteStandardStreamText(testDescriptorMock, streamLines, testResultMock) ==
                '''|
                   |  Hello
                   |  World \\[brackets\\] \u001B\\[0mANSI
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "suite stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new PlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            !theme.suiteStandardStreamText(testDescriptorMock, streamLines, testResultMock)
    }
}
