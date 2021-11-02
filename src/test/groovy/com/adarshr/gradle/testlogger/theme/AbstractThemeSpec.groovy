package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.InheritConstructors
import spock.lang.Unroll

import static java.lang.System.lineSeparator

class AbstractThemeSpec extends BaseThemeSpec {

    private static final Throwable TEST_EXCEPTION = generateException()

    private static final Throwable generateException() {
        def error = new Serializable() {
            Throwable one() { two() }

            Throwable two() { three() }

            Throwable three() {
                new RuntimeException('Middle error', new RuntimeException('Inner error'))
            }
        }

        lightenStackTrace(new RuntimeException('Outer error', error.one()))
    }

    private static Throwable lightenStackTrace(Throwable throwable) {
        throwable.stackTrace = throwable.stackTrace.findAll {
            it.className.contains 'adarshr'
        }

        if (throwable.cause) {
            lightenStackTrace(throwable.cause)
        }

        throwable
    }

    private static final int LINE_NUMBER = generateException().stackTrace.find { it.className == owner.name }.lineNumber

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
            theme.suiteStandardStreamText(testDescriptorMock, 'lines', testResultMock) == expected
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
            theme.testStandardStreamText(testDescriptorMock, 'lines', testResultMock) == expected
        where:
            expected                         | loggable
            'testStandardStreamTextInternal' | true
            ''                               | false
    }

    def "test exception text with default settings"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showCauses >> true
            testLoggerExtensionMock.showStackTraces >> true
            testDescriptorMock.className >> AbstractThemeSpec.name
            testResultMock.exception >> TEST_EXCEPTION
        when:
            def actual = theme.exceptionText(testDescriptorMock, testResultMock)
        then:
            actual ==
                """|
                   |
                   |  java.lang.RuntimeException: Outer error
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec.generateException(AbstractThemeSpec.groovy:${LINE_NUMBER})
                   |  Caused by: java.lang.RuntimeException: Middle error
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec.generateException(AbstractThemeSpec.groovy:${LINE_NUMBER})
                   |  Caused by: java.lang.RuntimeException: Inner error
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec.generateException(AbstractThemeSpec.groovy:${LINE_NUMBER})
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "test exception text with showFullStackTraces on"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showCauses >> true
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showFullStackTraces >> true
            testDescriptorMock.className >> AbstractThemeSpec.name
            testResultMock.exception >> TEST_EXCEPTION
        when:
            def actual = theme.exceptionText(testDescriptorMock, testResultMock)
        then:
            actual ==
                """|
                   |
                   |  java.lang.RuntimeException: Outer error
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec.generateException(AbstractThemeSpec.groovy:${LINE_NUMBER})
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec.<clinit>(AbstractThemeSpec.groovy:13)
                   |  Caused by: java.lang.RuntimeException: Middle error
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec\$1.three(AbstractThemeSpec.groovy:22)
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec\$1.two(AbstractThemeSpec.groovy:19)
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec\$1.one(AbstractThemeSpec.groovy:17)
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec\$1\$one.call(Unknown Source)
                   |      ... 2 more
                   |  Caused by: java.lang.RuntimeException: Inner error
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec\$1.three(AbstractThemeSpec.groovy:22)
                   |      ... 5 more
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "test exception text with showCauses off"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showCauses >> false
            testLoggerExtensionMock.showStackTraces >> true
            testLoggerExtensionMock.showFullStackTraces >> true
            testDescriptorMock.className >> AbstractThemeSpec.name
            testResultMock.exception >> TEST_EXCEPTION
        when:
            def actual = theme.exceptionText(testDescriptorMock, testResultMock)
        then:
            actual ==
                """|
                   |
                   |  java.lang.RuntimeException: Outer error
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec.generateException(AbstractThemeSpec.groovy:${LINE_NUMBER})
                   |      at com.adarshr.gradle.testlogger.theme.AbstractThemeSpec.<clinit>(AbstractThemeSpec.groovy:13)
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "test exception text with showStackTraces off"() {
        given:
            testLoggerExtensionMock.showExceptions >> true
            testLoggerExtensionMock.showCauses >> true
            testLoggerExtensionMock.showStackTraces >> false
            testDescriptorMock.className >> AbstractThemeSpec.name
            testResultMock.exception >> TEST_EXCEPTION
        when:
            def actual = theme.exceptionText(testDescriptorMock, testResultMock)
        then:
            actual ==
                """|
                   |
                   |  java.lang.RuntimeException: Outer error
                   |""".stripMargin().replace('\n', lineSeparator())
    }

    def "test exception text with showExceptions off"() {
        given:
            testLoggerExtensionMock.showExceptions >> false
            testDescriptorMock.className >> AbstractThemeSpec.name
            testResultMock.exception >> TEST_EXCEPTION
        when:
            def actual = theme.exceptionText(testDescriptorMock, testResultMock)
        then:
            !actual
    }

    @InheritConstructors
    static class TestTheme extends AbstractTheme {

        ThemeType type = null

        @Override
        protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
            'suiteTextInternal'
        }

        @Override
        protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
            'testTextInternal'
        }

        @Override
        protected suiteStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
            'suiteStandardStreamTextInternal'
        }

        @Override
        protected testStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
            'testStandardStreamTextInternal'
        }

        @Override
        String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
            'summaryText'
        }
    }
}
