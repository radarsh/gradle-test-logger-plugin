package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.api.tasks.testing.TestResult.ResultType

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaTheme implements Theme {

    @Override
    String beforeSuite(TestDescriptor descriptor) {
        "  [bold]${descriptor.className}[/]\n"
    }

    @Override
    String afterTest(TestDescriptor descriptor, TestResult result) {
        switch (result.resultType) {
            case SUCCESS: return "    [erase-ahead,green]${getSymbol(result.resultType)}[/] ${descriptor.name}"
            case FAILURE: return "    [erase-ahead,red]${getSymbol(result.resultType)} ${descriptor.name}[/]"
            case SKIPPED: return "    [erase-ahead,yellow]${getSymbol(result.resultType)} ${descriptor.name}[/]"
            default: return ''
        }
    }

    private static String getSymbol(ResultType resultType) {
        switch (resultType) {
            case SUCCESS: return windows ? '√' : '✔'
            case FAILURE: return windows ? 'X' : '✘'
            case SKIPPED: return windows ? '%' : '✂'
        }
    }

    private static boolean getWindows() {
        System.getProperty('os.name').startsWith('Windows')
    }
}
