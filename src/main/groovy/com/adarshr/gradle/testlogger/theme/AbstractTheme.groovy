package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper
import groovy.transform.CompileStatic

import static com.adarshr.gradle.testlogger.util.RendererUtils.escape
import static java.lang.System.lineSeparator

@CompileStatic
abstract class AbstractTheme implements Theme {

    final ThemeType type
    protected final TestLoggerExtension extension

    AbstractTheme(TestLoggerExtension extension) {
        this.type = extension.theme
        this.extension = extension
    }

    @Override
    final String suiteText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        result.loggable ? suiteTextInternal(descriptor) : ''
    }

    protected abstract String suiteTextInternal(TestDescriptorWrapper descriptor)

    @Override
    final String testText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        result.loggable ? testTextInternal(descriptor, result) : ''
    }

    protected abstract String testTextInternal(TestDescriptorWrapper descriptor, TestResultWrapper result)

    @Override
    String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result) {
        exceptionText(descriptor, result, 2)
    }

    @Override
    final String suiteStandardStreamText(String lines, TestResultWrapper result) {
        result.loggable ? suiteStandardStreamTextInternal(lines) : ''
    }

    protected abstract suiteStandardStreamTextInternal(String lines)

    @Override
    final String testStandardStreamText(String lines, TestResultWrapper result) {
        result.standardStreamLoggable ? testStandardStreamTextInternal(lines) : ''
    }

    protected abstract testStandardStreamTextInternal(String lines)

    protected String exceptionText(TestDescriptorWrapper descriptor, TestResultWrapper result, int indent) {
        def line = new StringBuilder()

        if (!extension.showExceptions) {
            return line
        }

        line << "${lineSeparator()}${lineSeparator()}"

        new StackTracePrinter(descriptor, ' ' * indent, line)
            .printStackTrace(result.exception)
            .toString()
    }

    private class StackTracePrinter {
        final TestDescriptorWrapper descriptor
        final String indentation
        final StringBuilder line

        StackTracePrinter(TestDescriptorWrapper descriptor, String indentation, StringBuilder line) {
            this.descriptor = descriptor
            this.indentation = indentation
            this.line = line
        }

        StackTracePrinter printStackTrace(Throwable exception, List<StackTraceElement> parentStackTrace = [], boolean cause = false) {
            if (cause) {
                line << "${indentation}Caused by: "
            }

            line << message(exception, cause)
            line << lineSeparator()

            if (!extension.showStackTraces) {
                return this
            }

            def filteredTrace = filter(exception.stackTrace)

            line << stackTrace(filteredTrace, countCommonFrames(parentStackTrace, filteredTrace))
            line << lineSeparator()

            if (extension.showCauses && exception.cause) {
                printStackTrace(exception.cause, filteredTrace, true)
            }

            this
        }

        String message(Throwable exception, boolean cause) {
            exception.toString()
                .trim()
                .readLines()
                .withIndex()
                .collect { String message, index ->
                    "${index != 0 || !cause ? indentation : ''}${escape(message)}"
                }.join(lineSeparator())
        }

        String stackTrace(List<StackTraceElement> stackTrace, int commonFrames) {
            def trace = new StringBuilder(stackTrace
                .subList(0, stackTrace.size() - commonFrames)
                .collect {
                    "${indentation}    at ${escape(it.toString())}"
                }.join(lineSeparator()))

            if (commonFrames) {
                trace << "${lineSeparator()}${indentation}    ... ${commonFrames} more"
            }

            trace.toString()
        }

        int countCommonFrames(List<StackTraceElement> parentStackTrace, List<StackTraceElement> stackTrace) {
            int count = 0

            if (parentStackTrace.empty) {
                return count;
            }

            int i = stackTrace.size() - 1, j = parentStackTrace.size() - 1

            while (i >= 1 && j >= 0 && stackTrace[i] == parentStackTrace[j]) {
                ++count; i--; j--
            }

            count
        }

        List<StackTraceElement> filter(StackTraceElement[] stackTrace) {
            if (extension.showFullStackTraces) {
                return stackTrace.toList()
            }

            stackTrace.find {
                it.className == descriptor.className
            }.collect {
                it as StackTraceElement
            }
        }

        @Override
        String toString() {
            line.toString()
        }
    }
}
