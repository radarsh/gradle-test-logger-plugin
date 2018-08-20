package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.util.TimeUtils
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static java.lang.System.lineSeparator

@SuppressWarnings("GrMethodMayBeStatic")
abstract class AbstractTheme implements Theme {

    final ThemeType type

    protected final boolean showExceptions
    protected final long slowThreshold
    protected final boolean showSummary
    protected final boolean showStandardStreams
    protected final boolean showPassed
    protected final boolean showSkipped
    protected final boolean showFailed

    AbstractTheme(TestLoggerExtension extension) {
        this.type = extension.theme
        this.showExceptions = extension.showExceptions
        this.slowThreshold = extension.slowThreshold
        this.showSummary = extension.showSummary
        this.showStandardStreams = extension.showStandardStreams
        this.showPassed = extension.showPassed
        this.showSkipped = extension.showSkipped
        this.showFailed = extension.showFailed
    }

    @Override
    final String suiteText(TestDescriptor descriptor, TestResult result) {
        canLog(result) ? suiteTextInternal(descriptor) : ''
    }

    protected abstract String suiteTextInternal(TestDescriptor descriptor)

    @Override
    final String testText(TestDescriptor descriptor, TestResult result) {
        canLog(result) ? testTextInternal(descriptor, result) : ''
    }

    protected abstract String testTextInternal(TestDescriptor descriptor, TestResult result)

    @Override
    String exceptionText(TestDescriptor descriptor, TestResult result) {
        exceptionText(descriptor, result, 2)
    }

    @Override
    final String suiteStandardStreamText(String lines, TestResult result) {
        canLog(result) ? suiteStandardStreamTextInternal(lines) : ''
    }

    protected abstract suiteStandardStreamTextInternal(String lines)

    @Override
    final String testStandardStreamText(String lines, TestResult result) {
        canLog(result) ? testStandardStreamTextInternal(lines) : ''
    }

    protected abstract testStandardStreamTextInternal(String lines)

    private boolean canLog(TestResult result) {
        showPassed && result.successfulTestCount ||
            showSkipped && result.skippedTestCount ||
            showFailed && result.failedTestCount
    }

    protected String escape(String text) {
        text
            .replace('\u001B', '')
            .replace('[', '\\[')
            .replace(']', '\\]')
    }

    protected String exceptionText(TestDescriptor descriptor, TestResult result, int indent) {
        def line = new StringBuilder()

        if (showExceptions) {
            def indentation = ' ' * indent

            line << "${lineSeparator()}${lineSeparator()}"

            line << result.exception.toString().trim().readLines().collect {
                "${indentation}${escape(it)}"
            }.join(lineSeparator())

            line << lineSeparator()

            line << result.exception.stackTrace.find {
                it.className == descriptor.className
            }.collect {
                "${indentation}    at ${escape(it.toString())}"
            }.join(lineSeparator())

            line << lineSeparator()
        }

        line
    }

    protected boolean tooSlow(TestResult result) {
        (result.endTime - result.startTime) >= slowThreshold
    }

    protected boolean mediumSlow(TestResult result) {
        (result.endTime - result.startTime) >= slowThreshold / 2
    }

    protected String duration(TestResult result) {
        TimeUtils.humanDuration(result.endTime - result.startTime)
    }

    protected String displayName(TestDescriptor descriptor) {
        escape(descriptor.properties.displayName ?: descriptor.name)
    }
}
