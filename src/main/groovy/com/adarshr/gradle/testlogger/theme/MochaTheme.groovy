package com.adarshr.gradle.testlogger.theme

import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.fusesource.jansi.Ansi.Erase.ALL
import static org.fusesource.jansi.Ansi.ansi
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class MochaTheme implements Theme {

    @Override
    String testCase(TestDescriptor descriptor) {
        ansi().bold().fgBrightYellow().a(descriptor.className).reset().toString()
    }

    @Override
    String beforeTest(TestDescriptor descriptor) {
        ansi().bold().a('  Test ').reset().a(descriptor.name).fgYellow().a(' \u2737').reset().cursorUpLine().toString()
    }

    @Override
    String afterTest(TestDescriptor descriptor, TestResult result) {
        def prefix = ansi().eraseLine(ALL).bold().a('  Test ').reset().a(descriptor.name)

        switch (result.resultType) {
            case SUCCESS: prefix.fgGreen().a(' \u2713'); break
            case FAILURE: prefix.fgRed().a(' \u2717'); break
            case SKIPPED: prefix.fgYellow().a(' \u2700'); break
        }

        prefix.reset().toString() + '|' + ('\u2713' as char)
    }
}
