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
    protected String suiteTextInternal(TestDescriptor descriptor) {
        "  [erase-ahead,default]${escape(descriptor.className)}[/]${lineSeparator()}"
    }

    @Override
    protected String testTextInternal(TestDescriptor descriptor, TestResult result) {
        testText('    [erase-ahead]', descriptor, result)
    }

    protected String testText(String start, TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder(start)

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
        exceptionText(descriptor, result, 6)
    }

    @Override
    protected String exceptionText(TestDescriptor descriptor, TestResult result, int indent) {
        def exceptionText = super.exceptionText(descriptor, result, indent)

        exceptionText ? "[red]${exceptionText}" : ''
    }

    @Override
    String summaryText(TestDescriptor descriptor, TestResult result) {
        return summaryText(descriptor, result, 2)
    }

    protected String summaryText(TestDescriptor descriptor, TestResult result, int indent) {
        if (!showSummary) {
            return ''
        }

        def indentation = ' ' * indent
        def line = new StringBuilder()

        line << "${indentation}[erase-ahead,green]${result.successfulTestCount} passing [grey](${duration(result)})"

        if (result.skippedTestCount) {
            line << "${lineSeparator()}${indentation}[erase-ahead,cyan]${result.skippedTestCount} pending"
        }
        if (result.failedTestCount) {
            line << "${lineSeparator()}${indentation}[erase-ahead,red]${result.failedTestCount} failing"
        }

        line << "[/]${lineSeparator()}"
    }

    @Override
    protected String suiteStandardStreamTextInternal(String lines) {
        standardStreamText(lines, 4)
    }

    @Override
    protected String testStandardStreamTextInternal(String lines) {
        standardStreamText(lines, 8)
    }

    protected String standardStreamText(String lines, int indent) {
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
