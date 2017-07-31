package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.fusesource.jansi.Ansi.Erase.ALL
import static org.fusesource.jansi.Ansi.ansi
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class StandardTheme implements Theme {

    @Override
    String testCase(TestDescriptor descriptor) {
        ansi().bold().fgBrightYellow().a(descriptor.className).reset().toString()
    }

    @Override
    String beforeTest(TestDescriptor descriptor) {
        ansi().bold().a('  Test ').reset().a(descriptor.name).fgYellow().a(' STARTED').reset().cursorUpLine().toString()
    }

    @Override
    String afterTest(TestDescriptor descriptor, TestResult result) {
        def prefix = ansi().eraseLine(ALL).bold().a('  Test ').reset().a(descriptor.name)

        switch (result.resultType) {
            case SUCCESS: prefix.fgGreen().a(' PASSED'); break
            case FAILURE: prefix.fgRed().a(' FAILED'); break
            case SKIPPED: prefix.fgYellow().a(' SKIPPED'); break
        }

        prefix.reset().toString()
    }
}
