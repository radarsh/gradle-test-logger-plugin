package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestResult.ResultType

import static com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
import static com.adarshr.gradle.testlogger.util.RendererUtils.escape
import static java.lang.System.lineSeparator
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@CompileStatic
@InheritConstructors
class MochaTheme extends AbstractTheme {

    ThemeType type = MOCHA

    @Override
    protected String suiteTextInternal(TestDescriptorWrapper descriptor) {
        "  ${'  ' * descriptor.depth}[erase-ahead,default]${descriptor.displayName}[/]${lineSeparator()}"
    }

    @Override
    protected String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        testTextInternal("  ${'  ' * descriptor.depth}[erase-ahead]", descriptor, result)
    }

    protected String testTextInternal(String start, TestDescriptorWrapper descriptor, TestResultWrapper result) {
        def line = new StringBuilder(start)

        switch (result.resultType) {
            case SUCCESS:
                line << "[green]${getSymbol(result.resultType)}[grey] ${descriptor.displayName}"
                showDurationIfSlow(result, line)
                break
            case FAILURE:
                line << "[red]${getSymbol(result.resultType)} ${descriptor.displayName}"
                showDurationIfSlow(result, line)
                line << exceptionText(descriptor, result)
                break
            case SKIPPED:
                line << "[cyan]${getSymbol(result.resultType)} ${descriptor.displayName}"
                break
        }

        line << '[/]'
    }

    private static void showDurationIfSlow(TestResultWrapper result, StringBuilder line) {
        if (result.tooSlow) {
            line << "[red] (${result.duration})"
        } else if (result.mediumSlow) {
            line << "[yellow] (${result.duration})"
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
    String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        exceptionText(descriptor, result, 6 * descriptor.depth)
    }

    @Override
    protected String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result, int indent) {
        def exceptionText = super.exceptionText(descriptor, result, indent)

        exceptionText ? "[red]${exceptionText}" : ''
    }

    @Override
    String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        return summaryText(descriptor, result, 2)
    }

    protected String summaryText(TestDescriptorWrapper descriptor, TestResultWrapper result, int indent) {
        if (!extension.showSummary) {
            return ''
        }

        def indentation = ' ' * indent
        def line = new StringBuilder()

        line << "${indentation}[erase-ahead,green]${result.successfulTestCount} passing [grey](${result.duration})"

        if (result.skippedTestCount) {
            line << "${lineSeparator()}${indentation}[erase-ahead,cyan]${result.skippedTestCount} pending"
        }
        if (result.failedTestCount) {
            line << "${lineSeparator()}${indentation}[erase-ahead,red]${result.failedTestCount} failing"
        }

        line << "[/]${lineSeparator()}"
    }

    @Override
    protected String suiteStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        standardStreamTextInternal(lines, descriptor.depth * 2 + 4)
    }

    @Override
    protected String testStandardStreamTextInternal(TestDescriptorWrapper descriptor, String lines) {
        standardStreamTextInternal(lines, descriptor.depth * 2 + 6)
    }

    protected String standardStreamTextInternal(String lines, int indent) {
        if (!extension.showStandardStreams || !lines) {
            return ''
        }

        lines = escape(lines)

        def indentation = ' ' * indent
        def line = new StringBuilder("[grey]${lineSeparator()}")

        line << lines.split($/${lineSeparator()}/$).collect {
            "${indentation}${it}"
        }.join(lineSeparator())

        line << "[/]${lineSeparator()}"
    }
}
