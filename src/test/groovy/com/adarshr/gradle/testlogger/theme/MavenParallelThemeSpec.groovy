package com.adarshr.gradle.testlogger.theme

import spock.lang.Unroll

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MavenParallelThemeSpec extends BaseThemeSpec {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final int LINE_NUMBER = exception.stackTrace.find { it.className == owner.name }.lineNumber

    Theme theme

    def setup() {
        theme = new MavenParallelTheme(testLoggerExtensionMock)
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
            SUCCESS    | '[erase-ahead,bold]ClassName[bold-off] test name[green] PASSED[/]'
            FAILURE    | '[erase-ahead,bold]ClassName[bold-off] test name[red] FAILED[/]'
            SKIPPED    | '[erase-ahead,bold]ClassName[bold-off] test name[yellow] SKIPPED[/]'
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showCauses >> true
            theme = new MavenParallelTheme(testLoggerExtensionMock)
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
                   |      at com.adarshr.gradle.testlogger.theme.MavenParallelThemeSpec.getException(MavenParallelThemeSpec.groovy:${
                    LINE_NUMBER
                })
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "standard stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new MavenParallelTheme(testLoggerExtensionMock)
        expect:
            theme.testStandardStreamText(streamLines, testResultMock) ==
                '''|[default]
                   |  Hello
                   |  World \\[brackets\\] \u001B\\[0mANSI[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "suite stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new MavenParallelTheme(testLoggerExtensionMock)
        expect:
            theme.suiteStandardStreamText(streamLines, testResultMock) ==
                '''|[default]
                   |  Hello
                   |  World \\[brackets\\] \u001B\\[0mANSI[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }
}
