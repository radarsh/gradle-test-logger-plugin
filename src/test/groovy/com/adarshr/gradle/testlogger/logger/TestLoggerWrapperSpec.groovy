package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.logging.Logger
import org.gradle.api.logging.configuration.ConsoleOutput
import org.gradle.api.tasks.testing.Test
import spock.lang.Specification

class TestLoggerWrapperSpec extends Specification {

    def extensionMock = Mock(TestLoggerExtension)
    def testMock = Mock(Test) {
        getProject() >> Mock(Project) {
            getGradle() >> Mock(Gradle) {
                getStartParameter() >> Mock(StartParameter) {
                    getConsoleOutput() >> ConsoleOutput.Auto
                }
            }
            getLogger() >> Mock(Logger)
        }
    }

    def "wrapper delegates to sequential test logger if parallel theme is not applied"() {
        given:
            extensionMock.theme >> ThemeType.STANDARD
        when:
            def wrapper = new TestLoggerWrapper(testMock, extensionMock)
        then:
            wrapper.testLoggerDelegate instanceof SequentialTestLogger
    }

    def "wrapper delegates to parallel test logger if parallel theme is applied"() {
        given:
            extensionMock.theme >> ThemeType.STANDARD_PARALLEL
        when:
            def wrapper = new TestLoggerWrapper(testMock, extensionMock)
        then:
            wrapper.testLoggerDelegate instanceof ParallelTestLogger
    }
}
