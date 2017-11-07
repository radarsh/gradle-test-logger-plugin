package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@InheritConstructors
class StandardTheme extends AbstractTheme {

    @Override
    String suiteText(TestDescriptor descriptor) {
        "[erase-ahead,bold,bright-yellow]${escape(descriptor.className)}[/]\n"
    }

    @Override
    String testText(TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder("[erase-ahead,bold]  Test [bold-off]${escape(descriptor.name)}")

        switch (result.resultType) {
            case SUCCESS:
                line << '[green] PASSED'
                if (tooSlow(result)) {
                    line << "[red] (${duration(result)})"
                } else if (mediumSlow(result)) {
                    line << "[yellow] (${duration(result)})"
                }
                break
            case FAILURE:
                line << '[red] FAILED'
                line << exceptionText(descriptor, result)
                break
            case SKIPPED:
                line << '[yellow] SKIPPED'
                break
        }

        line << '[/]'
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

        line << '[/]\n'
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
}
