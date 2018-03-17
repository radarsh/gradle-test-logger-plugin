package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestResult.ResultType

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@InheritConstructors
class MochaTheme extends AbstractTheme {

    @Override
    String suiteText(TestDescriptor descriptor) {
        "  [erase-ahead,default]${escape(descriptor.className)}[/]\n"
    }

    @Override
    String testText(TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder('    [erase-ahead]')

        switch (result.resultType) {
            case SUCCESS:
                line << "[green]${getSymbol(result.resultType)}[grey] ${escape(descriptor.name)}"
                showDurationIfSlow(result, line)
                break
            case FAILURE:
                line << "[red]${getSymbol(result.resultType)} ${escape(descriptor.name)}"
                showDurationIfSlow(result, line)
                line << exceptionText(descriptor, result)
                break
            case SKIPPED:
                line << "[cyan]${getSymbol(result.resultType)} ${escape(descriptor.name)}"
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
            line << "\n  [erase-ahead,cyan]${result.skippedTestCount} pending"
        }
        if (result.failedTestCount) {
            line << "\n  [erase-ahead,red]${result.failedTestCount} failing"
        }

        line << '[/]\n'
    }
}
