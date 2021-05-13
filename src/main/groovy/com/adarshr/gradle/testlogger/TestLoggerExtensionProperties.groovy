package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.logging.LogLevel

abstract class TestLoggerExtensionProperties {

    /**
     * Test logger theme. Defaults to {@link ThemeType#STANDARD}.
     */
    ThemeType theme

    /**
     * Log level used to print the messages. Defaults to {@link LogLevel#LIFECYCLE}
     */
    LogLevel logLevel

    /**
     * Whether exception information is to be shown. Defaults to true.
     */
    Boolean showExceptions

    /**
     * Whether causes of exceptions are to be shown. Defaults to true.
     */
    Boolean showCauses

    /**
     * Whether exception stack traces are to be shown. Defaults to true.
     */
    Boolean showStackTraces

    /**
     * Whether full (unfiltered) stack traces are to be shown. Defaults to false.
     */
    Boolean showFullStackTraces

    /**
     * Threshold in milliseconds to highlight slow tests. Defaults to 2000 milliseconds.
     */
    Long slowThreshold

    /**
     * Whether a summary of tests is shown in the end. Defaults to true.
     */
    Boolean showSummary

    /**
     * Whether standard streams are to be shown. Defaults to false.
     */
    Boolean showStandardStreams

    /**
     * Whether standard streams for passed tests are to be shown. Defaults to true.
     */
    Boolean showPassedStandardStreams

    /**
     * Whether standard streams for skipped tests are to be shown. Defaults to true.
     */
    Boolean showSkippedStandardStreams

    /**
     * Whether standard streams for failed tests are to be shown. Defaults to true.
     */
    Boolean showFailedStandardStreams

    /**
     * Whether passed tests should be shown. Defaults to true.
     */
    Boolean showPassed

    /**
     * Whether skipped tests should be shown. Defaults to true.
     */
    Boolean showSkipped

    /**
     * Whether failed tests should be shown. Defaults to true.
     */
    Boolean showFailed

    /**
     * Whether simple class names should be used for displaying test suites. Defaults to false.
     */
    Boolean showSimpleNames

    /**
     * Whether slow tests should be shown. Defaults to true.
     */
    Boolean showSlow
}
