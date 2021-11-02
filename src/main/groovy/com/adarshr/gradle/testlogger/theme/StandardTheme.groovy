package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static com.adarshr.gradle.testlogger.util.RendererUtils.escape
import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@CompileStatic
@InheritConstructors
class StandardTheme extends AbstractTheme {

    ThemeType type = STANDARD

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        "[erase-ahead,bold]${'  ' * descriptor.depth}${descriptor.displayName}[/]${lineSeparator()}"
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        testTextInternal("[erase-ahead,bold]${'  ' * descriptor.depth}Test [bold-off]${descriptor.displayName}", descriptor, result)
    }

    protected String testTextInternal(String start, TestDescriptorWrapper descriptor, TestResultWrapper result) {
        def line = new StringBuilder(start)

        switch (result.resultType) {
            case SUCCESS:
                line << '[green] PASSED'
                showDurationIfSlow(result, line)
                break
            case FAILURE:
                line << '[red] FAILED'
                showDurationIfSlow(result, line)
                line << exceptionText(descriptor, result)
                break
            case SKIPPED:
                line << '[yellow] SKIPPED'
                break
        }

        line << '[/]'
    }

    private void showDurationIfSlow(TestResultWrapper result, StringBuilder line) {
        if (result.tooSlow) {
            line << "[red] (${result.duration})"
        } else if (result.mediumSlow) {
            line << "[yellow] (${result.duration})"
        }
    }

    @Override
    protected String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result, int indent) {
        def exceptionText = super.exceptionText(descriptor, result, getType().parallel ? indent : indent * descriptor.depth)

        exceptionText ? "[red]${exceptionText}" : ''
    }

    @Override
    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        if (!extension.showSummary) {
            return ''
        }

        def colour = result.resultType == FAILURE ? 'red' : 'green'
        def line = new StringBuilder()

        line << "[erase-ahead,bold,${colour}]${result.resultType}: "
        line << "[default]Executed ${result.testCount} tests in ${result.duration}"

        def breakdown = getBreakdown(result)

        if (breakdown) {
            line << ' (' << breakdown.join(', ') << ')'
        }

        line << "[/]${lineSeparator()}"
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
        def line = new StringBuilder("[default]${lineSeparator()}")

        line << lines.split($/${lineSeparator()}/$).collect {
            "${indentation}${it}"
        }.join(lineSeparator())

        line << "[/]${lineSeparator()}"
    }
}
