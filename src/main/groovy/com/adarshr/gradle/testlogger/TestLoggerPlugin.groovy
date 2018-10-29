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
                def testLoggerExtension = buildTestLoggerExtension(test)

                test.testLogging.lifecycle.events = []

                def testLogger = new TestLoggerWrapper(project, test, testLoggerExtension)

                test.addTestListener(testLogger)
                test.addTestOutputListener(testLogger)
            }
        }
    }

    private static void createExtensions(Project project) {
        project.extensions.create('testlogger', TestLoggerExtension, project)
        project.tasks.withType(Test).each { task ->
            task.extensions.create('testlogger', TestLoggerExtension, project)
        }
        project.tasks.whenTaskAdded { task ->
            if (task instanceof Test) {
                task.extensions.create('testlogger', TestLoggerExtension, project)
            }
        }
    }

    private static TestLoggerExtension buildTestLoggerExtension(Test test) {
        def testExtension = test.testlogger as TestLoggerExtension
        def projectExtension = test.project.testlogger as TestLoggerExtension

        testExtension
            .undecorate()
            .reactTo(test.testLogging)
            .combine(projectExtension)
            .applyOverrides(overrides)
    }

    private static Map<String, String> getOverrides() {
        System.properties.findAll { key, value ->
            key.startsWith 'testlogger.'
        }.collectEntries { key, value ->
            [(key.replace('testlogger.', '')): value]
        } as Map<String, String>
    }
}
