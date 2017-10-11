package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class PlainThemeSpec extends Specification {

    def theme = new PlainTheme()
    def testDescriptorMock = Mock(TestDescriptor)
    def testResultMock = Mock(TestResult)

    def "before suite"() {
        given:
            testDescriptorMock.className >> 'ClassName'
        when:
            def actual = theme.beforeSuite(testDescriptorMock)
        then:
            actual == 'ClassName\n'
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
            SUCCESS    | '  Test test name \\[escaped\\] PASSED'
            FAILURE    | '  Test test name \\[escaped\\] FAILED'
            SKIPPED    | '  Test test name \\[escaped\\] SKIPPED'
    }
}
