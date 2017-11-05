package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll
import spock.util.environment.OperatingSystem

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaThemeSpec extends Specification {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final def ORIGINAL_OS = System.getProperty('os.name')

    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    Theme theme
    def testDescriptorMock = Mock(TestDescriptor)
    def testResultMock = Mock(TestResult)

    def setup() {
        testLoggerExtensionMock.slowThreshold >> 2000
        theme = new MochaTheme(testLoggerExtensionMock)
    }

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
                   |          at com.adarshr.gradle.testlogger.theme.MochaThemeSpec.getException(MochaThemeSpec.groovy:16)
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
                   |          at com.adarshr.gradle.testlogger.theme.MochaThemeSpec.getException(MochaThemeSpec.groovy:16)
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

    def "show time if slowThreshold is exceeded"() {
        given:
            testResultMock.resultType >> SUCCESS
            testResultMock.startTime >> 1000000
            testResultMock.endTime >> 1000000 + 10000
            testDescriptorMock.name >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == "    [erase-ahead,green]${symbol}[/] test name[red] (10s)[/]"
    }

    @Unroll
    def "summary text given #success success, #failure failed and #skipped skipped tests"() {
        given:
            testLoggerExtensionMock.showSummary >> true
            testResultMock.successfulTestCount >> success
            testResultMock.failedTestCount >> failure
            testResultMock.skippedTestCount >> skipped
            testResultMock.testCount >> success + failure + skipped
            testResultMock.startTime >> 1000000
            testResultMock.endTime >> 1000000 + 10000
            testResultMock.resultType >> (failure ? FAILURE : SUCCESS) // what Gradle would do
        and:
            theme = new MochaTheme(testLoggerExtensionMock)
        when:
            def actual = theme.summaryText(testDescriptorMock, testResultMock)
        then:
            actual == summaryText
        where:
            summaryText                                                            | success | failure | skipped
            '  [green]10 passing (10s)[/]\n'                                       | 10      | 0       | 0
            '  [green]5 passing (10s)\n  [yellow]2 pending[/]\n'                   | 5       | 0       | 2
            '  [green]5 passing (10s)\n  [red]3 failing[/]\n'                      | 5       | 3       | 0
            '  [green]5 passing (10s)\n  [yellow]2 pending\n  [red]3 failing[/]\n' | 5       | 3       | 2
    }

    def "summary when showSummary is false"() {
        expect:
            !theme.summaryText(testDescriptorMock, testResultMock)
    }

    private static String getSymbol() {
        OperatingSystem.current.windows ? '√' : '✔'
    }
}
