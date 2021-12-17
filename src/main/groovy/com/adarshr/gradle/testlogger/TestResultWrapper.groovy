package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.util.TimeUtils
import groovy.transform.CompileStatic
import org.gradle.api.tasks.testing.TestResult

@CompileStatic
class TestResultWrapper {

    @Delegate
    private final TestResult testResult
    private final TestLoggerExtension testLoggerExtension

    TestResultWrapper(TestResult testResult, TestLoggerExtension testLoggerExtension) {
        this.testResult = testResult
        this.testLoggerExtension = testLoggerExtension
    }

    boolean isLoggable() {
        boolean showPassed = testLoggerExtension.showPassed && testResult.successfulTestCount
        boolean showSkipped = testLoggerExtension.showSkipped && (testResult.resultType == TestResult.ResultType.SKIPPED || testResult.skippedTestCount)
        boolean showFailed = testLoggerExtension.showFailed && testResult.failedTestCount
        if (showPassed || showSkipped || showFailed) {
            boolean showSlow = testLoggerExtension.showOnlySlow && (!isTooSlow() || !isMediumSlow())
            if (showSlow) {
                return false
            }
            return true
        }
        return false
    }

    boolean isStandardStreamLoggable() {
        loggable && testLoggerExtension.showStandardStreams &&
            (testLoggerExtension.showPassedStandardStreams && testResult.successfulTestCount ||
                testLoggerExtension.showSkippedStandardStreams && testResult.skippedTestCount ||
                testLoggerExtension.showFailedStandardStreams && testResult.failedTestCount)
    }

    boolean isTooSlow() {
        (testResult.endTime - testResult.startTime) >= testLoggerExtension.slowThreshold
    }

    boolean isMediumSlow() {
        (testResult.endTime - testResult.startTime) >= testLoggerExtension.slowThreshold / 2
    }

    String getDuration() {
        TimeUtils.humanDuration(testResult.endTime - testResult.startTime)
    }
}
