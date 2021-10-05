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
        project.extensions.create(EXTENSION_NAME, TestLoggerExtension, project)

        project.tasks.withType(Test).configureEach { Test test ->
            def testExtension = test.extensions.create(EXTENSION_NAME, TestLoggerExtension, project, test)

            testExtension.originalTestLoggingEvents = test.testLogging.events
            test.testLogging.lifecycle.events = []

            def testLogger = new TestLoggerWrapper(project.gradle.startParameter, test, testExtension)

            test.addTestListener(testLogger)
            test.addTestOutputListener(testLogger)
        }
    }
}
