package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class StandardThemeSpec extends Specification {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    def theme = new StandardTheme(testLoggerExtensionMock)
    def testDescriptorMock = Mock(TestDescriptor)
    def testResultMock = Mock(TestResult)

    def "before suite"() {
        given:
            testDescriptorMock.className >> 'ClassName'
        when:
            def actual = theme.suiteText(testDescriptorMock)
        then:
            actual == '[bold,bright-yellow]ClassName[/]\n'
    }

    @Unroll
    def "after test with result type #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testDescriptorMock.name >> 'test name [escaped]'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            resultType | expected
            SUCCESS    | '[bold]  Test [/]test name \\[escaped\\][erase-ahead,green] PASSED[/]'
            FAILURE    | '[bold]  Test [/]test name \\[escaped\\][erase-ahead,red] FAILED[/]'
            SKIPPED    | '[bold]  Test [/]test name \\[escaped\\][erase-ahead,yellow] SKIPPED[/]'
    }

    def "after test with result type failure and showExceptions true"() {
        given:
            System.setProperty('os.name', 'Linux')
            testLoggerExtensionMock.showExceptions >> true
            theme = new StandardTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.name >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual ==
                '''|[bold]  Test [/]floppy test[erase-ahead,red] FAILED
                   |
                   |  java.lang.AssertionError: This is wrong
                   |      at com.adarshr.gradle.testlogger.theme.StandardThemeSpec.getException(StandardThemeSpec.groovy:15)
                   |[/]'''.stripMargin()
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            theme = new StandardTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.name >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        expect:
            theme.exceptionText(testDescriptorMock, testResultMock) ==
                '''|
                   |
                   |  java.lang.AssertionError: This is wrong
                   |      at com.adarshr.gradle.testlogger.theme.StandardThemeSpec.getException(StandardThemeSpec.groovy:15)
                   |'''.stripMargin()
    }

    def "exception text when showExceptions is false"() {
        given:
            testLoggerExtensionMock.showExceptions >> false
            testResultMock.resultType >> FAILURE
            testDescriptorMock.name >> 'floppy test'
        expect:
            !theme.exceptionText(testDescriptorMock, testResultMock)
    }
}
