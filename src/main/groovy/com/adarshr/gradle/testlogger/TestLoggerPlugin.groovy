package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.logger.TestLoggerWrapper
import groovy.transform.CompileStatic
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

@CompileStatic
class TestLoggerPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = 'testlogger'

    @Override
    void apply(Project project) {
        createExtensions(project)

        project.afterEvaluate {
            project.tasks.withType(Test).configureEach { test ->
                def testLoggerExtension = buildTestLoggerExtension(test)

                test.testLogging.lifecycle.events = []

                def testLogger = new TestLoggerWrapper(project, test, testLoggerExtension)

                test.addTestListener(testLogger)
                test.addTestOutputListener(testLogger)
            }
        }
    }

    private static void createExtensions(Project project) {
        project.extensions.create(EXTENSION_NAME, TestLoggerExtension, project)
        project.tasks.withType(Test).configureEach { task ->
            task.extensions.create(EXTENSION_NAME, TestLoggerExtension, project)
        }
    }

    private static TestLoggerExtension buildTestLoggerExtension(Test test) {
        def testExtension = test.extensions.findByName(EXTENSION_NAME) as TestLoggerExtension
        def projectExtension = test.project.extensions.findByName(EXTENSION_NAME) as TestLoggerExtension

        testExtension
            .undecorate()
            .reactTo(test.testLogging)
            .combine(projectExtension)
            .applyOverrides(overrides)
    }

    private static Map<String, String> getOverrides() {
        (System.properties as Map<String, String>).findAll { key, value ->
            key.startsWith "${EXTENSION_NAME}."
        }.collectEntries { key, value ->
            [(key.replace("${EXTENSION_NAME}.", '')): value]
        } as Map<String, String>
    }
}
