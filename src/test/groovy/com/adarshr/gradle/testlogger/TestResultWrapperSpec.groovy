package com.adarshr.gradle.testlogger

import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class TestResultWrapperSpec extends Specification {

    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    def testResultMock = Mock(TestResult)
    def wrapper = new TestResultWrapper(testResultMock, testLoggerExtensionMock)

    @Unroll
    def "loggable returns true if result type #resultType is turned on"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testResultMock.testCount >> totalCount
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        expect:
            wrapper.loggable
        where:
            resultType | totalCount | successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            SUCCESS    | 1          | 1               | 0            | 0           | true       | false       | false
            SKIPPED    | 1          | 0               | 1            | 0           | false      | true        | false
            FAILURE    | 1          | 0               | 0            | 1           | false      | false       | true
    }

    @Unroll
    def "loggable returns true if result type #resultType is turned off but there are results of other types too"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testResultMock.testCount >> totalCount
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        expect:
            wrapper.loggable
        where:
            resultType | totalCount | successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            SUCCESS    | 2          | 0               | 1            | 1           | false      | true        | true
            SKIPPED    | 2          | 1               | 0            | 1           | true       | false       | true
            FAILURE    | 2          | 1               | 1            | 0           | true       | true        | false
    }

    @Unroll
    def "loggable returns false if result type #resultType is turned off"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
        expect:
            !wrapper.loggable
        where:
            resultType | showPassed | showSkipped | showFailed
            SUCCESS    | false      | true        | true
            SKIPPED    | false      | true        | false
            FAILURE    | false      | false       | true
    }

    @Unroll
    def "is too slow returns #result if slow threshold is #slowThreshold"() {
        given:
            testResultMock.endTime >> 20000
            testResultMock.startTime >> 10000
            testLoggerExtensionMock.slowThreshold >> slowThreshold
        expect:
            wrapper.tooSlow == result
        where:
            slowThreshold | result
            10000         | true
            9999          | true
            10001         | false
    }

    @Unroll
    def "is medium slow returns #result if slow threshold is #slowThreshold"() {
        given:
            testResultMock.endTime >> 20000
            testResultMock.startTime >> 10000
            testLoggerExtensionMock.slowThreshold >> slowThreshold
        expect:
            wrapper.mediumSlow == result
        where:
            slowThreshold | result
            20001         | false
            20000         | true
            10000         | true
    }

    def "get duration"() {
        given:
            testResultMock.endTime >> 20000
            testResultMock.startTime >> 10000
        expect:
            wrapper.duration == '10s'
    }
}
