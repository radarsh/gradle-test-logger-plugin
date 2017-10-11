package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class StandardThemeSpec extends Specification {

    def theme = new StandardTheme()
    def testDescriptorMock = Mock(TestDescriptor)
    def testResultMock = Mock(TestResult)

    def "before suite"() {
        given:
            testDescriptorMock.className >> 'ClassName'
        when:
            def actual = theme.beforeSuite(testDescriptorMock)
        then:
            actual == '[bold,bright-yellow]ClassName[/]\n'
    }

    @Unroll
    def "after test with result type #resultType"() {
        given:
            testResultMock.resultType >> resultType
            testDescriptorMock.name >> 'test name [escaped]'
        when:
            def actual = theme.afterTest(testDescriptorMock, testResultMock)
        then:
            actual == expected
        where:
            resultType | expected
            SUCCESS    | '[bold]  Test [/]test name \\[escaped\\][erase-ahead,green] PASSED[/]'
            FAILURE    | '[bold]  Test [/]test name \\[escaped\\][erase-ahead,red] FAILED[/]'
            SKIPPED    | '[bold]  Test [/]test name \\[escaped\\][erase-ahead,yellow] SKIPPED[/]'
    }
}
