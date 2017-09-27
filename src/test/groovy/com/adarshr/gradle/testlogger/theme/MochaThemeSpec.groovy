package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaThemeSpec extends Specification {

    private static final def ORIGINAL_OS = System.getProperty('os.name')

    def theme = new MochaTheme()
    def testDescriptorMock = Mock(TestDescriptor)
    def testResultMock = Mock(TestResult)

    def cleanup() {
        System.setProperty('os.name', ORIGINAL_OS)
    }

    def "before suite"() {
        given:
            testDescriptorMock.className >> 'ClassName'
        when:
            def actual = theme.beforeSuite(testDescriptorMock)
        then:
            actual == '  [bold]ClassName[/]\n'
    }

    @Unroll
    def "after test with result type #resultType on #os"() {
        given:
            System.setProperty('os.name', os)
            testResultMock.resultType >> resultType
            testDescriptorMock.name >> 'method'
        when:
            def actual = theme.afterTest(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            os            | resultType | expected
            'Windows 8.1' | SUCCESS    | '    [erase-ahead,green]√[/] method'
            'Windows 8.1' | FAILURE    | '    [erase-ahead,red]X method[/]'
            'Windows 8.1' | SKIPPED    | '    [erase-ahead,yellow]% method[/]'
            'Linux'       | SUCCESS    | '    [erase-ahead,green]✔[/] method'
            'Linux'       | FAILURE    | '    [erase-ahead,red]✘ method[/]'
            'Linux'       | SKIPPED    | '    [erase-ahead,yellow]✂ method[/]'
    }
}
