package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification
import spock.lang.Unroll

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class AbstractThemeSpec extends Specification {

    Theme theme
    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    def testDescriptorMock = Mock(TestDescriptor)
    def testResultMock = Mock(TestResult)

    @Unroll
    def "suite text returns empty string if result type #resultType is turned off"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            !theme.suiteText(testDescriptorMock, testResultMock)
        where:
            resultType | showPassed | showSkipped | showFailed
            SUCCESS    | false      | true        | true
            SKIPPED    | false      | true        | false
            FAILURE    | false      | false       | true
    }

    @Unroll
    def "suite text returns actual value if result type #resultType is turned on"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testResultMock.testCount >> totalCount
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            theme.suiteText(testDescriptorMock, testResultMock) == 'suiteTextInternal'
        where:
            resultType | totalCount | successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            SUCCESS    | 1          | 1               | 0            | 0           | true       | false       | false
            SKIPPED    | 1          | 0               | 1            | 0           | false      | true        | false
            FAILURE    | 1          | 0               | 0            | 1           | false      | false       | true
    }

    @Unroll
    def "test text returns empty string if result type #resultType is turned off"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            !theme.testText(testDescriptorMock, testResultMock)
        where:
            resultType | showPassed | showSkipped | showFailed
            SUCCESS    | false      | true        | true
            SKIPPED    | false      | true        | false
            FAILURE    | false      | false       | true
    }

    @Unroll
    def "test text returns actual value if result type #resultType is turned on"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testResultMock.testCount >> totalCount
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            theme.testText(testDescriptorMock, testResultMock) == 'testTextInternal'
        where:
            resultType | totalCount | successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            SUCCESS    | 1          | 1               | 0            | 0           | true       | false       | false
            SKIPPED    | 1          | 0               | 1            | 0           | false      | true        | false
            FAILURE    | 1          | 0               | 0            | 1           | false      | false       | true
    }

    @Unroll
    def "suite standard stream text returns empty string if result type #resultType is turned off"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            !theme.suiteStandardStreamText('lines', testResultMock)
        where:
            resultType | showPassed | showSkipped | showFailed
            SUCCESS    | false      | true        | true
            SKIPPED    | false      | true        | false
            FAILURE    | false      | false       | true
    }

    @Unroll
    def "suite standard stream text returns actual value if result type #resultType is turned on"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testResultMock.testCount >> totalCount
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            theme.suiteStandardStreamText('lines', testResultMock) == 'suiteStandardStreamTextInternal'
        where:
            resultType | totalCount | successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            SUCCESS    | 1          | 1               | 0            | 0           | true       | false       | false
            SKIPPED    | 1          | 0               | 1            | 0           | false      | true        | false
            FAILURE    | 1          | 0               | 0            | 1           | false      | false       | true
    }

    @Unroll
    def "suite standard stream text returns actual value if result type #resultType is turned off but there are results of other types too"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testResultMock.testCount >> totalCount
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            theme.suiteStandardStreamText('lines', testResultMock) == 'suiteStandardStreamTextInternal'
        where:
            resultType | totalCount | successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            SUCCESS    | 2          | 0               | 1            | 1           | false      | true        | true
            SKIPPED    | 2          | 1               | 0            | 1           | true       | false       | true
            FAILURE    | 2          | 1               | 1            | 0           | true       | true        | false
    }

    @Unroll
    def "test standard stream text returns empty string if result type #resultType is turned off"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            !theme.testStandardStreamText('lines', testResultMock)
        where:
            resultType | showPassed | showSkipped | showFailed
            SUCCESS    | false      | true        | true
            SKIPPED    | false      | true        | false
            FAILURE    | false      | false       | true
    }

    @Unroll
    def "test standard stream text returns actual value if result type #resultType is turned on"() {
        given:
            testLoggerExtensionMock.showPassed >> showPassed
            testLoggerExtensionMock.showSkipped >> showSkipped
            testLoggerExtensionMock.showFailed >> showFailed
            testResultMock.testCount >> totalCount
            testResultMock.successfulTestCount >> successfulCount
            testResultMock.skippedTestCount >> skippedCount
            testResultMock.failedTestCount >> failedCount
        and:
            theme = new TestTheme(testLoggerExtensionMock)
        expect:
            theme.testStandardStreamText('lines', testResultMock) == 'testStandardStreamTextInternal'
        where:
            resultType | totalCount | successfulCount | skippedCount | failedCount | showPassed | showSkipped | showFailed
            SUCCESS    | 1          | 1               | 0            | 0           | true       | false       | false
            SKIPPED    | 1          | 0               | 1            | 0           | false      | true        | false
            FAILURE    | 1          | 0               | 0            | 1           | false      | false       | true
    }

    @InheritConstructors
    static class TestTheme extends AbstractTheme {

        @Override
        protected String suiteTextInternal(TestDescriptor descriptor) {
            'suiteTextInternal'
        }

        @Override
        protected String testTextInternal(TestDescriptor descriptor, TestResult result) {
            'testTextInternal'
        }

        @Override
        protected suiteStandardStreamTextInternal(String lines) {
            'suiteStandardStreamTextInternal'
        }

        @Override
        protected testStandardStreamTextInternal(String lines) {
            'testStandardStreamTextInternal'
        }

        @Override
        String summaryText(TestDescriptor descriptor, TestResult result) {
            'summaryText'
        }
    }
}
