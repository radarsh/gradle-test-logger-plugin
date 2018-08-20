package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@InheritConstructors
class PlainTheme extends AbstractTheme {

    protected static final Map RESULT_TYPE_MAPPING = [
        (SUCCESS): 'PASSED',
        (FAILURE): 'FAILED',
        (SKIPPED): 'SKIPPED'
    ]

    @Override
    protected String suiteTextInternal(TestDescriptor descriptor) {
        "${escape(descriptor.className)}${lineSeparator()}"
    }

    @Override
    protected String testTextInternal(TestDescriptor descriptor, TestResult result) {
        testText("  Test ${displayName(descriptor)} ${RESULT_TYPE_MAPPING[result.resultType]}", descriptor, result)
    }

    protected String testText(String start, TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder(start)

        if (tooSlow(result)) {
            line << " (${duration(result)})"
        }

        if (result.resultType == FAILURE) {
            line << exceptionText(descriptor, result)
        }

        line
    }

    @Override
    String summaryText(TestDescriptor descriptor, TestResult result) {
        if (!showSummary) {
            return ''
        }

        def line = new StringBuilder()

        line << "${result.resultType}: "
        line << "Executed ${result.testCount} tests in ${duration(result)}"

        def breakdown = getBreakdown(result)

        if (breakdown) {
            line << ' (' << breakdown.join(', ') << ')'
        }

        line << lineSeparator()
    }

    private static List getBreakdown(TestResult result) {
        def breakdown = []

        if (result.failedTestCount) {
            breakdown << "${result.failedTestCount} failed"
        }

        if (result.skippedTestCount) {
            breakdown << "${result.skippedTestCount} skipped"
        }

        breakdown
    }

    @Override
    protected String suiteStandardStreamTextInternal(String lines) {
        standardStreamText(lines, 2)
    }

    @Override
    protected String testStandardStreamTextInternal(String lines) {
        standardStreamText(lines, 4)
    }

    protected String standardStreamText(String lines, int indent) {
        if (!showStandardStreams || !lines) {
            return ''
        }

        lines = lines.replace('[', '\\[')

        def indentation = ' ' * indent
        def line = new StringBuilder(lineSeparator())

        line << lines.split($/${lineSeparator()}/$).collect {
            "${indentation}${it}"
        }.join(lineSeparator())

        line << lineSeparator()
    }
}
