package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.logger.TestLoggerWrapper
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestLoggerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        createExtensions(project)

        project.afterEvaluate {
            project.tasks.withType(Test).each { test ->
                def testExtension = test.extensions.testlogger as TestLoggerExtension
                def projectExtension = test.project.extensions.testlogger as TestLoggerExtension
                def combinedExtension = testExtension.combine(projectExtension)

                combinedExtension.applyOverrides()

                test.testLogging.lifecycle.events = []

                def testLogger = new TestLoggerWrapper(project, test, combinedExtension)

                test.addTestListener(testLogger)
                test.addTestOutputListener(testLogger)
            }
        }
    }

    private static void createExtensions(Project project) {
        project.extensions.create('testlogger', TestLoggerExtension, project, overrides)
        project.tasks.withType(Test).each { task ->
            task.extensions.create('testlogger', TestLoggerExtension, project, overrides)
        }
        project.tasks.whenTaskAdded { task ->
            if (task instanceof Test) {
                task.extensions.create('testlogger', TestLoggerExtension, project, overrides)
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
