package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class StandardTheme implements Theme {

    @Override
    String testCase(TestDescriptor descriptor) {
        "[bold,bright-yellow]${descriptor.className}[/]"
    }

    @Override
    String beforeTest(TestDescriptor descriptor) {
        "[bold]  Test [/]${descriptor.name}[yellow] STARTED[/][cursor-up-line]"
        ""
    }

    @Override
    String afterTest(TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder("[cursor-up-line,bold]  Test [/]${descriptor.name}")

        switch (result.resultType) {
            case SUCCESS: line << '[green] PASSED'; break
            case FAILURE: line << '[bright-red] FAILED'; break
            case SKIPPED: line << '[yellow] SKIPPED'; break
        }

        line << '[/]'
    }
}
