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
        "  [default]${escape(descriptor.className)}[/]\n"
    }

    @Override
    String testText(TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder()

        switch (result.resultType) {
            case SUCCESS:
                line << "    [erase-ahead,green]${getSymbol(result.resultType)}[grey] ${escape(descriptor.name)}[/]"
                if (tooSlow(result)) {
                    line << "[red] (${duration(result)})[/]"
                } else if (mediumSlow(result)) {
                    line << "[yellow] (${duration(result)})[/]"
                }
                break
            case FAILURE:
                line << "    [erase-ahead,red]${getSymbol(result.resultType)} ${escape(descriptor.name)}[/]"
                line << exceptionText(descriptor, result)
                break
            case SKIPPED:
                line << "    [erase-ahead,cyan]${getSymbol(result.resultType)} ${escape(descriptor.name)}[/]"
                break
        }

        line
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
        def line = super.exceptionText(descriptor, result, 6)
        line ? "[red]${line}[/]" : ''
    }

    @Override
    String summaryText(TestDescriptor descriptor, TestResult result) {
        if (!showSummary) {
            return ''
        }

        def line = new StringBuilder()
        line << "  [green]${result.successfulTestCount} passing [grey](${duration(result)})"

        if (result.skippedTestCount) {
            line << "\n  [cyan]${result.skippedTestCount} pending"
        }
        if (result.failedTestCount) {
            line << "\n  [red]${result.failedTestCount} failing"
        }

        line << '[/]\n'
    }
}
