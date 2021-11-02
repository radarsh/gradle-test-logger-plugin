package com.adarshr.gradle.testlogger.theme


import spock.lang.Unroll
import spock.util.environment.OperatingSystem

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaThemeSpec extends BaseThemeSpec {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final int LINE_NUMBER = exception.stackTrace.find { it.className == owner.name }.lineNumber
    private static final def ORIGINAL_OS = System.getProperty('os.name')

    Theme theme

    def setup() {
        theme = new MochaTheme(testLoggerExtensionMock)
    }

    def cleanup() {
        System.setProperty('os.name', ORIGINAL_OS)
    }

    def "suite text"() {
        given:
            testDescriptorMock.displayName >> 'ClassName'
            testDescriptorMock.depth >> 0
        when:
            def actual = theme.suiteText(testDescriptorMock, testResultMock)
        then:
            actual == "  [erase-ahead,default]ClassName[/]${lineSeparator()}"
    }

    @Unroll
    def "after test with result type #resultType on #os"() {
        given:
            System.setProperty('os.name', os)
            testResultMock.resultType >> resultType
            testDescriptorMock.displayName >> 'test name'
            testDescriptorMock.depth >> 1
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            os            | resultType | expected
            'Windows 8.1' | SUCCESS    | '    [erase-ahead][green]√[grey] test name[/]'
            'Windows 8.1' | FAILURE    | '    [erase-ahead][red]X test name[/]'
            'Windows 8.1' | SKIPPED    | '    [erase-ahead][cyan]- test name[/]'
            'Linux'       | SUCCESS    | '    [erase-ahead][green]✔[grey] test name[/]'
            'Linux'       | FAILURE    | '    [erase-ahead][red]✘ test name[/]'
            'Linux'       | SKIPPED    | '    [erase-ahead][cyan]- test name[/]'
    }

    def "after test with result type failure and showExceptions true"() {
        given:
            System.setProperty('os.name', 'Linux')
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showCauses >> true
            theme = new MochaTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.displayName >> 'floppy test'
            testDescriptorMock.className >> this.class.name
            testDescriptorMock.depth >> 1
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual ==
                """|    [erase-ahead][red]✘ floppy test[red]
                   |
                   |      java.lang.AssertionError: This is wrong
                   |          at com.adarshr.gradle.testlogger.theme.MochaThemeSpec.getException(MochaThemeSpec.groovy:${LINE_NUMBER})
                   |[/]""".stripMargin().replace('\n', lineSeparator())
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showCauses >> true
            theme = new MochaTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.displayName >> 'floppy test'
            testDescriptorMock.className >> this.class.name
            testDescriptorMock.depth >> 1
        expect:
            theme.exceptionText(testDescriptorMock, testResultMock) ==
                """|[red]
                   |
                   |      java.lang.AssertionError: This is wrong
                   |          at com.adarshr.gradle.testlogger.theme.MochaThemeSpec.getException(MochaThemeSpec.groovy:${LINE_NUMBER})
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "exception text when showExceptions is false"() {
        given:
            testLoggerExtensionMock.showExceptions >> false
            testResultMock.resultType >> FAILURE
            testDescriptorMock.name >> 'floppy test'
            testDescriptorMock.depth >> 1
        expect:
            !theme.exceptionText(testDescriptorMock, testResultMock)
    }

    def "exception text when showExceptions is true but exception is null"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> null
            testDescriptorMock.displayName >> 'exception is null test'
            testDescriptorMock.depth >> 1
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
            testDescriptorMock.depth >> 1
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == text
        where:
            resultType | text
            SUCCESS    | "    [erase-ahead][green]${passedSymbol}[grey] test name[red] (10s)[/]"
            FAILURE    | "    [erase-ahead][red]${failedSymbol} test name[red] (10s)[/]"
    }

    @Unroll
    def "show duration if slowThreshold is approaching for resultType #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testResultMock.duration >> '1.5s'
            testResultMock.mediumSlow >> true
            testDescriptorMock.displayName >> 'test name'
            testDescriptorMock.depth >> 1
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == text
        where:
            resultType | text
            SUCCESS    | "    [erase-ahead][green]${passedSymbol}[grey] test name[yellow] (1.5s)[/]"
            FAILURE    | "    [erase-ahead][red]${failedSymbol} test name[yellow] (1.5s)[/]"
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
            theme = new MochaTheme(testLoggerExtensionMock)
        when:
            def actual = theme.summaryText(testDescriptorMock, testResultMock)
        then:
            actual == summaryText.stripMargin().replace('\n', lineSeparator())
        where:
            //@formatter:off
            summaryText                                                 | success | failure | skipped
            '''|  [erase-ahead,green]10 passing [grey](10s)[/]
               |'''                                                     | 10      | 0       | 0

            '''|  [erase-ahead,green]5 passing [grey](10s)
               |  [erase-ahead,cyan]2 pending[/]
               |'''                                                     | 5       | 0       | 2

            '''|  [erase-ahead,green]5 passing [grey](10s)
               |  [erase-ahead,red]3 failing[/]
               |'''                                                     | 5       | 3       | 0

            '''|  [erase-ahead,green]5 passing [grey](10s)
               |  [erase-ahead,cyan]2 pending
               |  [erase-ahead,red]3 failing[/]
               |'''                                                     | 5       | 3       | 2
            //@formatter:on
    }

    def "summary when showSummary is false"() {
        expect:
            !theme.summaryText(testDescriptorMock, testResultMock)
    }

    def "standard stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new MochaTheme(testLoggerExtensionMock)
        expect:
            theme.testStandardStreamText(testDescriptorMock, streamLines, testResultMock) ==
                '''|[grey]
                   |      Hello
                   |      World \\[brackets\\] \u001B\\[0mANSI[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "standard stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new MochaTheme(testLoggerExtensionMock)
        expect:
            !theme.testStandardStreamText(testDescriptorMock, streamLines, testResultMock)
    }

    def "suite stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new MochaTheme(testLoggerExtensionMock)
        expect:
            theme.suiteStandardStreamText(testDescriptorMock, streamLines, testResultMock) ==
                '''|[grey]
                   |    Hello
                   |    World \\[brackets\\] \u001B\\[0mANSI[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "suite stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new MochaTheme(testLoggerExtensionMock)
        expect:
            !theme.suiteStandardStreamText(testDescriptorMock, streamLines, testResultMock)
    }

    private static String getPassedSymbol() {
        OperatingSystem.current.windows ? '√' : '✔'
    }

    private static String getFailedSymbol() {
        OperatingSystem.current.windows ? 'X' : '✘'
    }
}
