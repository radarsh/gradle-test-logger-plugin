package com.adarshr.gradle.testlogger.theme


import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Unroll

import static java.lang.System.lineSeparator
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
            def testResultMock = Mock(TestResult)
            setupResultTypeExpectation(testResultMock, resultType)
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name [escaped]'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            resultType | expected
            SUCCESS    | 'ClassName test name \\[escaped\\] PASSED'
            FAILURE    | 'ClassName test name \\[escaped\\] FAILED'
            SKIPPED    | 'ClassName test name \\[escaped\\] SKIPPED'
    }

    def "after test with result type failure and showExceptions true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            theme = new PlainParallelTheme(testLoggerExtensionMock)
        and:
            def testResultMock = Mock(TestResult)
            setupResultTypeExpectation(testResultMock, FAILURE)
            testResultMock.exception >> exception
            testDescriptorMock.name >> 'floppy test'
            testDescriptorMock.className >> this.class.name
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

    def "after test uses displayName property if present"() {
        given:
            testDescriptorMock = GroovyMock(TestDescriptor)
            testDescriptorMock.properties >> [displayName: 'display test name [escaped]']
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name [escaped]'
        expect:
            theme.testText(testDescriptorMock, testResultMock) == 'ClassName display test name \\[escaped\\] PASSED'
    }

    def "after test does not error when displayName property is missing"() {
        given:
            testDescriptorMock = GroovyMock(TestDescriptor)
            testDescriptorMock.properties >> [:]
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name [escaped]'
        expect:
            theme.testText(testDescriptorMock, testResultMock) == 'ClassName test name \\[escaped\\] PASSED'
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            theme = new PlainParallelTheme(testLoggerExtensionMock)
        and:
            def testResultMock = Mock(TestResult)
            setupResultTypeExpectation(testResultMock, FAILURE)
            testResultMock.exception >> exception
            testDescriptorMock.name >> 'floppy test'
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
            def testResultMock = Mock(TestResult)
            setupResultTypeExpectation(testResultMock, FAILURE)
            testDescriptorMock.name >> 'floppy test'
        expect:
            !theme.exceptionText(testDescriptorMock, testResultMock)
    }

    def "show duration if slowThreshold is exceeded"() {
        given:
            testResultMock.startTime >> 1000000
            testResultMock.endTime >> 1000000 + 10000
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == 'ClassName test name PASSED (10s)'
    }

    @Unroll
    def "summary text given #success success, #failure failed and #skipped skipped tests"() {
        given:
            testLoggerExtensionMock.showSummary >> true
            def testResultMock = Mock(TestResult)
            testResultMock.successfulTestCount >> success
            testResultMock.failedTestCount >> failure
            testResultMock.skippedTestCount >> skipped
            testResultMock.testCount >> success + failure + skipped
            testResultMock.startTime >> 1000000
            testResultMock.endTime >> 1000000 + 10000
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
            theme.testStandardStreamText(streamLines, testResultMock) ==
                '''|
                   |  Hello
                   |  World
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "standard stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new PlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            !theme.testStandardStreamText(streamLines, testResultMock)
    }

    def "suite stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new PlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            theme.suiteStandardStreamText(streamLines, testResultMock) ==
                '''|
                   |  Hello
                   |  World
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "suite stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new PlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            !theme.suiteStandardStreamText(streamLines, testResultMock)
    }
}
