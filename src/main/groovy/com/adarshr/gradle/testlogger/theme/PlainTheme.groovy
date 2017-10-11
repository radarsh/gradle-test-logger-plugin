package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class PlainTheme extends AbstractTheme {

    private static final Map RESULT_TYPE_MAPPING = [
        (SUCCESS): 'PASSED',
        (FAILURE): 'FAILED',
        (SKIPPED): 'SKIPPED'
    ]

    @Override
    String beforeSuite(TestDescriptor descriptor) {
        "${escape(descriptor.className)}\n"
    }

    @Override
    String afterTest(TestDescriptor descriptor, TestResult result) {
        "  Test ${escape(descriptor.name)} ${RESULT_TYPE_MAPPING[result.resultType]}"
    }
}
