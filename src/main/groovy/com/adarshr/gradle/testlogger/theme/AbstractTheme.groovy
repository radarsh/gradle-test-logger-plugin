package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

@SuppressWarnings("GrMethodMayBeStatic")
abstract class AbstractTheme implements Theme {

    protected final boolean showExceptions

    AbstractTheme(TestLoggerExtension extension) {
        this.showExceptions = extension.showExceptions
    }

    @Override
    String exceptionText(TestDescriptor descriptor, TestResult result) {
        exceptionText(descriptor, result, 2)
    }

    protected String escape(String text) {
        text
            .replace('\u001B', '')
            .replace('[', '\\[')
            .replace(']', '\\]')
    }

    protected String exceptionText(TestDescriptor descriptor, TestResult result, int indent) {
        def line = new StringBuilder()
        def indentation = ' ' * indent

        if (showExceptions) {
            line << '\n\n'

            line << result.exception.toString().trim().readLines().collect {
                "${indentation}${escape(it)}"
            }.join('\n')

            line << '\n'

            line << result.exception.stackTrace.find {
                it.className == descriptor.className
            }.collect {
                "${indentation}    at ${escape(it.toString())}"
            }.join('\n')

            line << '\n'
        }

        line
    }
}
