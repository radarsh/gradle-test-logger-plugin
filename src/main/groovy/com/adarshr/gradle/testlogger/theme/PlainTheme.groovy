package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.util.RendererUtils.escape
import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@CompileStatic
@InheritConstructors
class PlainTheme extends AbstractTheme {

    ThemeType type = PLAIN

    protected static final Map RESULT_TYPE_MAPPING = [
        (SUCCESS): 'PASSED',
        (FAILURE): 'FAILED',
        (SKIPPED): 'SKIPPED'
    ]

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        "${'  ' * descriptor.depth}${descriptor.displayName}${lineSeparator()}"
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        testTextInternal("${'  ' * descriptor.depth}Test ${descriptor.displayName} ${RESULT_TYPE_MAPPING[result.resultType]}", descriptor, result)
    }

    protected String testTextInternal(String start, TestDescriptorWrapper descriptor, TestResultWrapper result) {
        def line = new StringBuilder(start)

        if (result.tooSlow) {
            line << " (${result.duration})"
        }

        if (result.resultType == FAILURE) {
            line << exceptionText(descriptor, result)
        }

        line
    }

    @Override
    protected String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result, int indent) {
        super.exceptionText(descriptor, result, getType().parallel ? indent : indent * descriptor.depth)
    }

    @Override
    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        if (!extension.showSummary) {
            return ''
        }

        def line = new StringBuilder()

        line << "${result.resultType}: "
        line << "Executed ${result.testCount} tests in ${result.duration}"

        def breakdown = getBreakdown(result)

        if (breakdown) {
            line << ' (' << breakdown.join(', ') << ')'
        }

        line << lineSeparator()
    }

    private static List getBreakdown(TestResultWrapper result) {
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
    protected String suiteStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        standardStreamTextInternal(lines, descriptor.depth * 2 + 2)
    }

    @Override
    protected String testStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        standardStreamTextInternal(lines, descriptor.depth * 2 + 2)
    }

    protected String standardStreamTextInternal(String lines, int indent) {
        if (!extension.showStandardStreams || !lines) {
            return ''
        }

        lines = escape(lines)

        def indentation = ' ' * indent
        def line = new StringBuilder(lineSeparator())

        line << lines.split($/${lineSeparator()}/$).collect {
            "${indentation}${it}"
        }.join(lineSeparator())

        line << lineSeparator()
    }
}
