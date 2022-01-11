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
        and:
            testResultMock.resultType >> resultType
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
            SKIPPED    | 0          | 0               | 0            | 0           | false      | true        | false
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
    def "standardStreamLoggable returns #expected if loggable is true and showStandardStreams is true"() {
        given:
            testLoggerExtensionMock.showPassed >> true
            testLoggerExtensionMock.showSkipped >> true
            testLoggerExtensionMock.showFailed >> true
            testLoggerExtensionMock.showStandardStreams >> true
            testLoggerExtensionMock.showPassedStandardStreams >> showPassedStdStr
            testLoggerExtensionMock.showSkippedStandardStreams >> showSkippedStdStr
            testLoggerExtensionMock.showFailedStandardStreams >> showFailedStdStr
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        expect:
            wrapper.standardStreamLoggable == expected
        where:
            showPassedStdStr | successfulCount | showSkippedStdStr | skippedCount | showFailedStdStr | failedCount | expected
            true             | 1               | false             | 0            | false            | 0           | true
            false            | 1               | false             | 0            | false            | 0           | false
            false            | 0               | true              | 1            | false            | 0           | true
            false            | 0               | false             | 1            | false            | 0           | false
            false            | 0               | false             | 0            | true             | 1           | true
            false            | 0               | false             | 0            | false            | 1           | false
    }

    @Unroll
    def "standardStreamLoggable returns false if loggable is #loggable and showStandardStreams is #showStandardStreams"() {
        given:
            testLoggerExtensionMock.showPassed >> loggable
            testLoggerExtensionMock.showSkipped >> loggable
            testLoggerExtensionMock.showFailed >> loggable
            testLoggerExtensionMock.showStandardStreams >> showStandardStreams
            testResultMock.successfulTestCount >> 1
            testResultMock.skippedTestCount >> 1
            testResultMock.failedTestCount >> 1
        expect:
            !wrapper.standardStreamLoggable
        where:
            loggable | showStandardStreams
            false    | true
            true     | false
            false    | false
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
    def "loggable returns false if showOnlySlow is turned #showOnlySlow"() {
        given:
            testResultMock.endTime >> 20000
            testResultMock.startTime >> 10000
            testResultMock.successfulTestCount >> 1
            testResultMock.failedTestCount >> 1
            testResultMock.skippedTestCount >> 1
            testLoggerExtensionMock.slowThreshold >> slowThreshold
            testLoggerExtensionMock.showOnlySlow >> showOnlySlow
            testLoggerExtensionMock.showFailed >> showFailed
            testLoggerExtensionMock.showPassed >> showPassed
        expect:
            wrapper.isLoggable() == result
        where:
            slowThreshold | showOnlySlow | showPassed | showFailed | result
            10000         | true         | true       | true       | true
            9999          | false        | true       | true       | true
            10000         | true         | true       | false      | true
            9999          | false        | true       | false      | true
            10000         | true         | false      | true       | true
            9999          | false        | false      | true       | true
            10000         | true         | false      | false      | false
            9999          | false        | false      | false      | false
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

    @Unroll
    def "loggable returns true if test is too slow but the type is disabled"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testLoggerExtensionMock.showOnlySlow >> true
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
            testResultMock.endTime >> 20000
            testResultMock.startTime >> 10000
        expect:
            !wrapper.loggable
        where:
            successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            1               | 0            | 0           | false      | true        | true
            0               | 1            | 0           | true       | false       | true
            0               | 0            | 1           | true       | true        | false
    }
}
