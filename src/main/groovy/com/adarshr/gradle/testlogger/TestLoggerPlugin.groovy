package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.logger.TestLoggerWrapper
import groovy.transform.CompileStatic
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.util.GradleVersion

@CompileStatic
class TestLoggerPlugin implements Plugin<Project> {

    private static final String EXTENSION_NAME = 'testlogger'
    private static final String MIN_GRADLE = "7.6"

    @Override
    void apply(Project project) {
        if (GradleVersion.current() <= GradleVersion.version(MIN_GRADLE)) {
            throw new GradleException("Gradle Test Logger Plugin requires Gradle version ${MIN_GRADLE} or above.")
        }

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
