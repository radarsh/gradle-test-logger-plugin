package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestResult.ResultType

import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@InheritConstructors
class MochaTheme extends AbstractTheme {

    @Override
    String suiteText(TestDescriptor descriptor) {
        "  [erase-ahead,default]${escape(descriptor.className)}[/]${lineSeparator()}"
    }

    @Override
    String testText(TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder('    [erase-ahead]')

        switch (result.resultType) {
            case SUCCESS:
                line << "[green]${getSymbol(result.resultType)}[grey] ${displayName(descriptor)}"
                showDurationIfSlow(result, line)
                break
            case FAILURE:
                line << "[red]${getSymbol(result.resultType)} ${displayName(descriptor)}"
                showDurationIfSlow(result, line)
                line << exceptionText(descriptor, result)
                break
            case SKIPPED:
                line << "[cyan]${getSymbol(result.resultType)} ${displayName(descriptor)}"
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

    private static String getSymbol(ResultType resultType) {
        switch (resultType) {
            case SUCCESS: return windows ? '√' : '✔'
            case FAILURE: return windows ? 'X' : '✘'
            case SKIPPED: return '-'
        }
    }

    private static boolean getWindows() {
        System.getProperty('os.name').startsWith('Windows')
    }

    @Override
    String exceptionText(TestDescriptor descriptor, TestResult result) {
        def exceptionText = super.exceptionText(descriptor, result, 6)

        exceptionText ? "[red]${exceptionText}" : ''
    }

    @Override
    String summaryText(TestDescriptor descriptor, TestResult result) {
        if (!showSummary) {
            return ''
        }

        def line = new StringBuilder()
        line << "  [erase-ahead,green]${result.successfulTestCount} passing [grey](${duration(result)})"

        if (result.skippedTestCount) {
            line << "${lineSeparator()}  [erase-ahead,cyan]${result.skippedTestCount} pending"
        }
        if (result.failedTestCount) {
            line << "${lineSeparator()}  [erase-ahead,red]${result.failedTestCount} failing"
        }

        line << "[/]${lineSeparator()}"
    }

    @Override
    String suiteStandardStreamText(String lines) {
        standardStreamText(lines, 4)
    }

    @Override
    String testStandardStreamText(String lines) {
        standardStreamText(lines, 8)
    }

    private String standardStreamText(String lines, int indent) {
        if (!showStandardStreams || !lines) {
            return ''
        }

        lines = lines.replace('[', '\\[')

        def indentation = ' ' * indent
        def line = new StringBuilder("[grey]${lineSeparator()}")

        line << lines.split($/${lineSeparator()}/$).collect {
            "${indentation}${it}"
        }.join(lineSeparator())

        line << "[/]${lineSeparator()}"
    }
}
