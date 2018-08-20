package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@InheritConstructors
class StandardTheme extends AbstractTheme {

    @Override
    protected String suiteTextInternal(TestDescriptor descriptor) {
        "[erase-ahead,bold]${escape(descriptor.className)}[/]${lineSeparator()}"
    }

    @Override
    protected String testTextInternal(TestDescriptor descriptor, TestResult result) {
        testText("[erase-ahead,bold]  Test [bold-off]${displayName(descriptor)}", descriptor, result)
    }

    protected String testText(String start, TestDescriptor descriptor, TestResult result) {
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

    private void showDurationIfSlow(TestResult result, StringBuilder line) {
        if (tooSlow(result)) {
            line << "[red] (${duration(result)})"
        } else if (mediumSlow(result)) {
            line << "[yellow] (${duration(result)})"
        }
    }

    @Override
    protected String exceptionText(TestDescriptor descriptor, TestResult result, int indent) {
        def exceptionText = super.exceptionText(descriptor, result, indent)

        exceptionText ? "[red]${exceptionText}" : ''
    }

    @Override
    String summaryText(TestDescriptor descriptor, TestResult result) {
        if (!showSummary) {
            return ''
        }

        def colour = result.resultType == FAILURE ? 'red' : 'green'
        def line = new StringBuilder()

        line << "[erase-ahead,bold,${colour}]${result.resultType}: "
        line << "[default]Executed ${result.testCount} tests in ${duration(result)}"

        def breakdown = getBreakdown(result)

        if (breakdown) {
            line << ' (' << breakdown.join(', ') << ')'
        }

        line << "[/]${lineSeparator()}"
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
        def line = new StringBuilder("[default]${lineSeparator()}")

        line << lines.split($/${lineSeparator()}/$).collect {
            "${indentation}${it}"
        }.join(lineSeparator())

        line << "[/]${lineSeparator()}"
    }
}
