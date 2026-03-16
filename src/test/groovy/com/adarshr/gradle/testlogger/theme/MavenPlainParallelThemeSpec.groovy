package com.adarshr.gradle.testlogger.theme

import spock.lang.Unroll

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MavenPlainParallelThemeSpec extends BaseThemeSpec {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final int LINE_NUMBER = exception.stackTrace.find { it.className == owner.name }.lineNumber

    Theme theme

    def setup() {
        theme = new MavenPlainParallelTheme(testLoggerExtensionMock)
    }

    def "before suite"() {
        expect:
            !theme.suiteText(testDescriptorMock, testResultMock)
    }

    @Unroll
    def "after test with result type #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testDescriptorMock.classDisplayName >> 'ClassName'
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

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showCauses >> true
            theme = new MavenPlainParallelTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.displayName >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        expect:
            theme.exceptionText(testDescriptorMock, testResultMock) ==
                """|
                   |
                   |  java.lang.AssertionError: This is wrong
                   |      at com.adarshr.gradle.testlogger.theme.MavenPlainParallelThemeSpec.getException(MavenPlainParallelThemeSpec.groovy:${LINE_NUMBER})
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "standard stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new MavenPlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            theme.testStandardStreamText(streamLines, testResultMock) ==
                '''|
                   |  Hello
                   |  World \\[brackets\\] \u001B\\[0mANSI
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "suite stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new MavenPlainParallelTheme(testLoggerExtensionMock)
            testResultMock.resultType >> SUCCESS
        expect:
            theme.suiteStandardStreamText(streamLines, testResultMock) ==
                '''|
                   |  Hello
                   |  World \\[brackets\\] \u001B\\[0mANSI
                   |'''.stripMargin().replace('\n', lineSeparator())
    }
}
