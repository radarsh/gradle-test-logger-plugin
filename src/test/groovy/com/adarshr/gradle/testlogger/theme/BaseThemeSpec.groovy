package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper
import spock.lang.Specification

import static java.lang.System.lineSeparator

abstract class BaseThemeSpec extends Specification {

    protected def testLoggerExtensionMock = Mock(TestLoggerExtension)
    protected def testDescriptorMock = Mock(TestDescriptorWrapper)
    protected def testResultMock = Mock(TestResultWrapper)
    protected def streamLines = "Hello${lineSeparator()}World [brackets] \u001B[0mANSI"

    def setup() {
        testResultMock.loggable >> true
        testResultMock.standardStreamLoggable >> true
        testLoggerExtensionMock.slowThreshold >> 2000
    }
}
