package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

import static com.adarshr.gradle.testlogger.util.RendererUtils.preserveAnsi
import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@InheritConstructors
@CompileStatic
class StandardTheme extends AbstractTheme {

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        "[erase-ahead,bold]${descriptor.classDisplayName}[/]${lineSeparator()}"
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        testTextInternal("[erase-ahead,bold]  Test [bold-off]${descriptor.displayName}", descriptor, result)
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
        def exceptionText = super.exceptionText(descriptor, result, indent)

        exceptionText ? "[red]${exceptionText}" : ''
    }

    @Override
    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        if (!showSummary) {
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
    protected String suiteStandardStreamTextInternal(String lines) {
        standardStreamTextInternal(lines, 2)
    }

    @Override
    protected String testStandardStreamTextInternal(String lines) {
        standardStreamTextInternal(lines, 4)
    }

    protected String standardStreamTextInternal(String lines, int indent) {
        if (!showStandardStreams || !lines) {
            return ''
        }

        lines = preserveAnsi(lines)

        def indentation = ' ' * indent
        def line = new StringBuilder("[default]${lineSeparator()}")

        line << lines.split($/${lineSeparator()}/$).collect {
            "${indentation}${it}"
        }.join(lineSeparator())

        line << "[/]${lineSeparator()}"
    }
}
