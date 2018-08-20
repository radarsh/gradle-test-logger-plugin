package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import spock.lang.Specification

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.FAILURE
import static org.gradle.api.tasks.testing.TestResult.ResultType.SKIPPED
import static org.gradle.api.tasks.testing.TestResult.ResultType.SUCCESS


abstract class BaseThemeSpec extends Specification {

    protected def testLoggerExtensionMock = Mock(TestLoggerExtension)
    protected def testDescriptorMock = Mock(TestDescriptor)
    protected def testResultMock = Mock(TestResult)
    protected def streamLines = "Hello${lineSeparator()}World"

    def setup() {
        testResultMock.resultType >> SUCCESS
        testResultMock.successfulTestCount >> 1
        testLoggerExtensionMock.slowThreshold >> 2000
        testLoggerExtensionMock.showPassed >> true
        testLoggerExtensionMock.showSkipped >> true
        testLoggerExtensionMock.showFailed >> true
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    protected void setupResultTypeExpectation(TestResult testResultMock, TestResult.ResultType resultType) {
        testResultMock.resultType >> resultType

        switch (resultType) {
            case SUCCESS:
                testResultMock.successfulTestCount >> 1
                break
            case SKIPPED:
                testResultMock.skippedTestCount >> 1
                break
            case FAILURE:
                testResultMock.failedTestCount >> 1
                break
        }
    }
}
