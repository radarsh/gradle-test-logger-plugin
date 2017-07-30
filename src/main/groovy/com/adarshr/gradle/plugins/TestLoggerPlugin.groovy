package com.adarshr.gradle.plugins

import com.adarshr.gradle.plugins.theme.Theme
import com.adarshr.gradle.plugins.theme.ThemeFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult

class TestLoggerPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create('testlogger', TestLoggerExtension)

        project.tasks.find {it.name == 'test'}.doFirst {
            it.configure(getTestConfiguration(project))
        }
    }

    private static Closure getTestConfiguration(project) {
        Set classes = []

        test {
            maxParallelForks = 1
            testLogging.lifecycle.events = []
//            jvmArgs '-Dfile.encoding=UTF-8'
//            systemProperty 'file.encoding', 'UTF-8'

            Theme theme = ThemeFactory.loadTheme(project.testlogger.theme)

            beforeTest { TestDescriptor descriptor ->
                if (!classes.contains(descriptor.className)) {
                    classes << descriptor.className
                    project.logger.lifecycle theme.testCase(descriptor)
                }

                project.logger.lifecycle theme.beforeTest(descriptor)
            }

            afterTest { TestDescriptor descriptor, TestResult result ->
                project.logger.lifecycle theme.afterTest(descriptor, result)
            }
        }
    }

    private static Closure test(@DelegatesTo(Test) closure) {
        closure
    }
}
