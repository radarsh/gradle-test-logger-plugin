package com.adarshr.gradle.testlogger.theme


import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Unroll
import spock.util.environment.OperatingSystem

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaParallelThemeSpec extends BaseThemeSpec {

    // right at the top to minimise line number changes
    private static AssertionError getException() {
        new AssertionError('This is wrong')
    }

    private static final int LINE_NUMBER = exception.stackTrace.find { it.className == owner.name }.lineNumber
    private static final def ORIGINAL_OS = System.getProperty('os.name')

    Theme theme

    def setup() {
        theme = new MochaParallelTheme(testLoggerExtensionMock)
    }

    def cleanup() {
        System.setProperty('os.name', ORIGINAL_OS)
    }

    def "before suite"() {
        expect:
            !theme.suiteText(testDescriptorMock, testResultMock)
    }

    @Unroll
    def "after test with result type #resultType on #os"() {
        given:
            System.setProperty('os.name', os)
            def testResultMock = Mock(TestResult)
            setupResultTypeExpectation(testResultMock, resultType)
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name [escaped]'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            os            | resultType | expected
            'Windows 8.1' | SUCCESS    | '  [erase-ahead,default]ClassName [green]√[grey] test name \\[escaped\\][/]'
            'Windows 8.1' | FAILURE    | '  [erase-ahead,default]ClassName [red]X test name \\[escaped\\][/]'
            'Windows 8.1' | SKIPPED    | '  [erase-ahead,default]ClassName [cyan]- test name \\[escaped\\][/]'
            'Linux'       | SUCCESS    | '  [erase-ahead,default]ClassName [green]✔[grey] test name \\[escaped\\][/]'
            'Linux'       | FAILURE    | '  [erase-ahead,default]ClassName [red]✘ test name \\[escaped\\][/]'
            'Linux'       | SKIPPED    | '  [erase-ahead,default]ClassName [cyan]- test name \\[escaped\\][/]'
    }

    def "after test with result type failure and showExceptions true"() {
        given:
            System.setProperty('os.name', 'Linux')
            testLoggerExtensionMock.showExceptions >> true
            theme = new MochaParallelTheme(testLoggerExtensionMock)
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
                """|  [erase-ahead,default]com.adarshr.gradle.testlogger.theme.MochaParallelThemeSpec [red]✘ floppy test[red]
                   |
                   |    java.lang.AssertionError: This is wrong
                   |        at com.adarshr.gradle.testlogger.theme.MochaParallelThemeSpec.getException(MochaParallelThemeSpec.groovy:${LINE_NUMBER})
                   |[/]""".stripMargin().replace('\n', lineSeparator())
    }

    def "after test uses displayName property if present"() {
        given:
            System.setProperty('os.name', 'Linux')
        and:
            testDescriptorMock = GroovyMock(TestDescriptor)
            testDescriptorMock.properties >> [displayName: 'display test name [escaped]']
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name [escaped]'
        expect:
            theme.testText(testDescriptorMock, testResultMock) == '  [erase-ahead,default]ClassName [green]✔[grey] display test name \\[escaped\\][/]'
    }

    def "after test does not error when displayName property is missing"() {
        given:
            System.setProperty('os.name', 'Linux')
        and:
            testDescriptorMock = GroovyMock(TestDescriptor)
            testDescriptorMock.properties >> [:]
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name [escaped]'
        expect:
            theme.testText(testDescriptorMock, testResultMock) == '  [erase-ahead,default]ClassName [green]✔[grey] test name \\[escaped\\][/]'
    }

    def "exception text when showExceptions is true"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            theme = new MochaParallelTheme(testLoggerExtensionMock)
        and:
            testResultMock.resultType >> FAILURE
            testResultMock.exception >> exception
            testDescriptorMock.name >> 'floppy test'
            testDescriptorMock.className >> this.class.name
        expect:
            theme.exceptionText(testDescriptorMock, testResultMock) ==
                """|[red]
                   |
                   |    java.lang.AssertionError: This is wrong
                   |        at com.adarshr.gradle.testlogger.theme.MochaParallelThemeSpec.getException(MochaParallelThemeSpec.groovy:${LINE_NUMBER})
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

    @Unroll
    def "show duration if slowThreshold is exceeded for resultType #resultType"() {
        given:
            def testResultMock = Mock(TestResult)
            setupResultTypeExpectation(testResultMock, resultType)
            testResultMock.startTime >> 1000000
            testResultMock.endTime >> 1000000 + 10000
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == text
        where:
            resultType | text
            SUCCESS    | "  [erase-ahead,default]ClassName [green]${passedSymbol}[grey] test name[red] (10s)[/]"
            FAILURE    | "  [erase-ahead,default]ClassName [red]${failedSymbol} test name[red] (10s)[/]"
    }

    @Unroll
    def "show duration if slowThreshold is approaching for resultType #resultType"() {
        given:
            def testResultMock = Mock(TestResult)
            setupResultTypeExpectation(testResultMock, resultType)
            testResultMock.startTime >> 1000000
            testResultMock.endTime >> 1000000 + 1500 // slow threshold is 2s
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name'
        when:
            def actual = theme.testText(testDescriptorMock, testResultMock)
        then:
            actual == text
        where:
            resultType | text
            SUCCESS    | "  [erase-ahead,default]ClassName [green]${passedSymbol}[grey] test name[yellow] (1.5s)[/]"
            FAILURE    | "  [erase-ahead,default]ClassName [red]${failedSymbol} test name[yellow] (1.5s)[/]"
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
            theme = new MochaParallelTheme(testLoggerExtensionMock)
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
            theme = new MochaParallelTheme(testLoggerExtensionMock)
        expect:
            theme.testStandardStreamText(streamLines, testResultMock) ==
                '''|[grey]
                   |    Hello
                   |    World[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "standard stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new MochaParallelTheme(testLoggerExtensionMock)
        expect:
            !theme.testStandardStreamText(streamLines, testResultMock)
    }

    def "suite stream text"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> true
            theme = new MochaParallelTheme(testLoggerExtensionMock)
        expect:
            theme.suiteStandardStreamText(streamLines, testResultMock) ==
                '''|[grey]
                   |    Hello
                   |    World[/]
                   |'''.stripMargin().replace('\n', lineSeparator())
    }

    def "suite stream text when showStandardStreams is false"() {
        given:
            testLoggerExtensionMock.showStandardStreams >> false
            theme = new MochaParallelTheme(testLoggerExtensionMock)
        expect:
            !theme.suiteStandardStreamText(streamLines, testResultMock)
    }

    private static String getPassedSymbol() {
        OperatingSystem.current.windows ? '√' : '✔'
    }

    private static String getFailedSymbol() {
        OperatingSystem.current.windows ? 'X' : '✘'
    }
}
