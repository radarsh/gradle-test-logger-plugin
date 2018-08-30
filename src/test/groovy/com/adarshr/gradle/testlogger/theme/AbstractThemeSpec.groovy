package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.InheritConstructors
import spock.lang.Specification
import spock.lang.Unroll

class AbstractThemeSpec extends Specification {

    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    def theme = new TestTheme(testLoggerExtensionMock)
    def testDescriptorMock = Mock(TestDescriptorWrapper)
    def testResultMock = Mock(TestResultWrapper)

    @Unroll
    def "suite text returns '#expected' when loggable is #loggable"() {
        given:
            testResultMock.loggable >> loggable
        expect:
            theme.suiteText(testDescriptorMock, testResultMock) == expected
        where:
            expected            | loggable
            'suiteTextInternal' | true
            ''                  | false
    }

    @Unroll
    def "test text returns '#expected' when loggable is #loggable"() {
        given:
            testResultMock.loggable >> loggable
        expect:
            theme.testText(testDescriptorMock, testResultMock) == expected
        where:
            expected           | loggable
            'testTextInternal' | true
            ''                 | false
    }

    @Unroll
    def "suite standard stream text returns '#expected' when loggable is #loggable"() {
        given:
            testResultMock.loggable >> loggable
        expect:
            theme.suiteStandardStreamText('lines', testResultMock) == expected
        where:
            expected                          | loggable
            'suiteStandardStreamTextInternal' | true
            ''                                | false
    }

    @Unroll
    def "test standard stream text returns '#expected' when loggable is #loggable"() {
        given:
            testResultMock.standardStreamLoggable >> loggable
        expect:
            theme.testStandardStreamText('lines', testResultMock) == expected
        where:
            expected                         | loggable
            'testStandardStreamTextInternal' | true
            ''                               | false
    }

    @InheritConstructors
    static class TestTheme extends AbstractTheme {

        @Override
        protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
            'suiteTextInternal'
        }

        @Override
        protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
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
        String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
            'summaryText'
        }
    }
}
