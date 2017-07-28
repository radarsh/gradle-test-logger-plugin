package com.adarshr.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import static org.fusesource.jansi.Ansi.Erase.ALL
import static org.fusesource.jansi.Ansi.ansi
import static org.gradle.api.tasks.testing.TestResult.ResultType.*

class TestLoggerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Set classes = [] as Set

        project.tasks.find { it.name == 'test' }.configure(getTestConfiguration(project, classes))
    }

    private static Closure getTestConfiguration(project, Set classes) {
        test {
            maxParallelForks = 1
            testLogging.lifecycle.events = []

            def log = { project.logger.lifecycle(it.toString()) }

            beforeTest { TestDescriptor descriptor ->
                if (!classes.contains(descriptor.className)) {
                    classes << descriptor.className
                    log ansi().bold().fgBrightYellow().a(descriptor.className).reset()
                }

                log ansi().bold().a('  Test ').fgDefault().a(descriptor.name).fgYellow().a(' STARTED').reset().cursorUpLine()
            }

            afterTest { TestDescriptor descriptor, TestResult result ->
                def prefix = ansi().eraseLine(ALL).bold().a('  Test ').fgDefault().a(descriptor.name)

                switch (result.resultType) {
                    case SUCCESS: prefix.fgGreen().a(' PASSED'); break
                    case FAILURE: prefix.fgRed().a(' FAILED'); break
                    case SKIPPED: prefix.fgYellow().a(' SKIPPED'); break
                }

                log prefix.reset()
            }
        }
    }

    private static Closure test(@DelegatesTo(Test) closure) {
        closure
    }
}
