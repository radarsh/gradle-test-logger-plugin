package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper

import static com.adarshr.gradle.testlogger.util.RendererUtils.escape
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
    final String suiteText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        result.loggable ? suiteTextInternal(descriptor) : ''
    }

    protected abstract String suiteTextInternal(TestDescriptorWrapper descriptor)

    @Override
    final String testText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        result.loggable ? testTextInternal(descriptor, result) : ''
    }

    protected abstract String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result)

    @Override
    String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        exceptionText(descriptor, result, 2)
    }

    @Override
    final String suiteStandardStreamText(String lines, TestResultWrapper result) {
        result.loggable ? suiteStandardStreamTextInternal(lines) : ''
    }

    protected abstract suiteStandardStreamTextInternal(String lines)

    @Override
    final String testStandardStreamText(String lines, TestResultWrapper result) {
        result.loggable ? testStandardStreamTextInternal(lines) : ''
    }

    protected abstract testStandardStreamTextInternal(String lines)

    protected String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result, int indent) {
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
}
