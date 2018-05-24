package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.util.TimeUtils
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static java.lang.System.lineSeparator

@SuppressWarnings("GrMethodMayBeStatic")
abstract class AbstractTheme implements Theme {

    protected final boolean showExceptions
    protected final long slowThreshold
    protected final boolean showSummary
    protected final boolean showStandardStreams

    AbstractTheme(TestLoggerExtension extension) {
        this.showExceptions = extension.showExceptions
        this.slowThreshold = extension.slowThreshold
        this.showSummary = extension.showSummary
        this.showStandardStreams = extension.showStandardStreams
    }

    @Override
    String exceptionText(TestDescriptor descriptor, TestResult result) {
        exceptionText(descriptor, result, 2)
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
