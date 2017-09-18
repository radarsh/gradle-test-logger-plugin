package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaTheme implements Theme {

    @Override
    String beforeSuite(TestDescriptor descriptor) {
        "  [bold]${descriptor.className}[/]\n"
    }

    @Override
    String afterTest(TestDescriptor descriptor, TestResult result) {
        switch (result.resultType) {
            case SUCCESS: return "    [erase-ahead,green]✔[/] ${descriptor.name}"
            case FAILURE: return "    [erase-ahead,red]✘ ${descriptor.name}[/]"
            case SKIPPED: return "    [erase-ahead,yellow]! ${descriptor.name}[/]"
            default: return ''
        }
    }
}
