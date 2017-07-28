package com.adarshr.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

import java.text.DecimalFormat

import static org.fusesource.jansi.Ansi.Erase
import static org.fusesource.jansi.Ansi.ansi

class TestLoggerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        Set classes = [] as Set

        project.tasks.find { it.name == 'test' }.configure {
            maxParallelForks = 1

            testLogging {
                lifecycle.events = []
            }

            beforeTest { TestDescriptor descriptor ->
                if (classes.contains(descriptor.className)) {
                    project.logger.lifecycle ansi().bold().a('  Test ').reset().fgDefault().a(descriptor.name).reset().fgYellow().a(' STARTED').reset().cursorUpLine().toString()
                } else {
                    classes << descriptor.className
                    project.logger.lifecycle ansi().bold().fgBrightYellow().a(descriptor.className).reset().toString()

                    project.logger.lifecycle ansi().bold().a('  Test ').reset().fgDefault().a(descriptor.name).reset().fgYellow().a(' STARTED').reset().cursorUpLine().toString()
                }
            }
            afterTest { TestDescriptor descriptor, TestResult result ->
                def duration = " - ${new DecimalFormat('#.##').format((result.endTime - result.startTime) / 1000)}s"

                if (result.resultType.name() == 'SUCCESS') {
                    project.logger.lifecycle ansi().eraseLine(Erase.ALL).bold().a('  Test ').reset().fgDefault().a(descriptor.name).reset().fgGreen().a(' PASSED').fgDefault().a(duration).reset().toString()
                } else if (result.resultType.name() == 'FAILURE') {
                    project.logger.lifecycle ansi().eraseLine(Erase.ALL).bold().a('  Test ').reset().fgDefault().a(descriptor.name).reset().fgRed().a(' FAILED').fgDefault().a(duration).reset().toString()
                } else {
                    project.logger.lifecycle ansi().eraseLine(Erase.ALL).bold().a('  Test ').reset().fgDefault().a(descriptor.name).reset().fgYellow().a(' SKIPPED').fgDefault().a(duration).reset().toString()
                }
            }
        }
    }
}
