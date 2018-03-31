package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.logger.TestEventLogger
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestLoggerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('testlogger', TestLoggerExtension, project)

        project.afterEvaluate {
            project.tasks.withType(Test).each { test ->
                assertSequentialTestExecution(test)

                test.testLogging.lifecycle.events = []

                def testEventLogger = new TestEventLogger(project)

                test.addTestListener(testEventLogger)
                test.addTestOutputListener(testEventLogger)
            }
        }
    }

    private static void assertSequentialTestExecution(Test test) {
        if (test.maxParallelForks != 1) {
            throw new GradleException('Parallel execution is not supported')
        }
    }
}
