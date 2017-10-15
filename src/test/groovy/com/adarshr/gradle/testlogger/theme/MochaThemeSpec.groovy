package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaThemeSpec extends Specification {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final def ORIGINAL_OS = System.getProperty('os.name')

    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    def theme = new MochaTheme(testLoggerExtensionMock)
    def testDescriptorMock = Mock(TestDescriptor)
    def testResultMock = Mock(TestResult)

    def cleanup() {
        System.setProperty('os.name', ORIGINAL_OS)
    }

    def "before suite"() {
        given:
            testDescriptorMock.className >> 'ClassName'
        when:
            def actual = theme.suiteText(testDescriptorMock)
        then:
            actual == '  [bold]ClassName[/]\n'
    }

    @Unroll
    def "after test with result type #resultType on #os"() {
        given:
            System.setProperty('os.name', os)
            testResultMock.resultType >> resultType
            testDescriptorMock.name >> 'test name [escaped]'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            os            | resultType | expected
            'Windows 8.1' | SUCCESS    | '    [erase-ahead,green]√[/] test name \\[escaped\\]'
            'Windows 8.1' | FAILURE    | '    [erase-ahead,red]X test name \\[escaped\\][/]'
            'Windows 8.1' | SKIPPED    | '    [erase-ahead,yellow]% test name \\[escaped\\][/]'
            'Linux'       | SUCCESS    | '    [erase-ahead,green]✔[/] test name \\[escaped\\]'
            'Linux'       | FAILURE    | '    [erase-ahead,red]✘ test name \\[escaped\\][/]'
            'Linux'       | SKIPPED    | '    [erase-ahead,yellow]✂ test name \\[escaped\\][/]'
    }

    def "after test with result type failure and showExceptions true"() {
        given:
            System.setProperty('os.name', 'Linux')
            testLoggerExtensionMock.showExceptions >> true
            theme = new MochaTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.name >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual ==
                '''|    [erase-ahead,red]✘ floppy test[/][red]
                   |
                   |      java.lang.AssertionError: This is wrong
                   |          at com.adarshr.gradle.testlogger.theme.MochaThemeSpec.getException(MochaThemeSpec.groovy:15)
                   |[/]'''.stripMargin()
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            theme = new MochaTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.name >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        expect:
            theme.exceptionText(testDescriptorMock, testResultMock) ==
                '''|[red]
                   |
                   |      java.lang.AssertionError: This is wrong
                   |          at com.adarshr.gradle.testlogger.theme.MochaThemeSpec.getException(MochaThemeSpec.groovy:15)
                   |[/]'''.stripMargin()
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
