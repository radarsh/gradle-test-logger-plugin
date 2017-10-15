package com.adarshr.gradle.testlogger.theme

import groovy.transform.InheritConstructors
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

@InheritConstructors
class StandardTheme extends AbstractTheme {

    @Override
    String suiteText(TestDescriptor descriptor) {
        "[bold,bright-yellow]${escape(descriptor.className)}[/]\n"
    }

    @Override
    String testText(TestDescriptor descriptor, TestResult result) {
        def line = new StringBuilder("[bold]  Test [/]${escape(descriptor.name)}")

        switch (result.resultType) {
            case SUCCESS:
                line << '[erase-ahead,green] PASSED'
                break
            case FAILURE:
                line << '[erase-ahead,red] FAILED'
                line << exceptionText(descriptor, result)
                break
            case SKIPPED:
                line << '[erase-ahead,yellow] SKIPPED'
                break
        }

        line << '[/]'
    }
}
