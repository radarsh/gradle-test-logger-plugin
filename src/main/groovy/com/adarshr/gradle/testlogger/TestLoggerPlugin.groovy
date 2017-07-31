package com.adarshr.gradle.testlogger

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test

class TestLoggerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('testlogger', TestLoggerExtension)

        Test test = project.tasks.getByName('test') as Test

        test.maxParallelForks = 1
        test.testLogging.lifecycle.events = []
        test.addTestListener(new TestLoggerTestListener(project))
    }
}
