package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class StandardTheme extends AbstractTheme {

    @Override
    String beforeSuite(TestDescriptor descriptor) {
        "[bold,bright-yellow]${escape(descriptor.className)}[/]\n"
    }

    @Override
    String afterTest(TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder("[bold]  Test [/]${escape(descriptor.name)}")

        switch (result.resultType) {
            case SUCCESS: line << '[erase-ahead,green] PASSED'; break
            case FAILURE: line << '[erase-ahead,red] FAILED'; break
            case SKIPPED: line << '[erase-ahead,yellow] SKIPPED'; break
        }

        line << '[/]'
    }
}
