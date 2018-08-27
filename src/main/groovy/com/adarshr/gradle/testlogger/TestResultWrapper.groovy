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
        testLoggerExtension.showPassed && successfulTestCount ||
            testLoggerExtension.showSkipped && skippedTestCount ||
            testLoggerExtension.showFailed && failedTestCount
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
