package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.logger.TestLoggerWrapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestLoggerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('testlogger', TestLoggerExtension, project, overrides)

        project.afterEvaluate {
            project.tasks.withType(Test).each { test ->
                project.testlogger.applyOverrides()

                test.testLogging.lifecycle.events = []

                def testLogger = new TestLoggerWrapper(project, test)

                test.addTestListener(testLogger)
                test.addTestOutputListener(testLogger)
            }
        }
    }

    private static Map<String, String> getOverrides() {
        System.properties.findAll { key, value ->
            key.startsWith 'testlogger.'
        }.collectEntries { key, value ->
            [(key.replace('testlogger.', '')): value]
        } as Map<String, String>
    }
}
