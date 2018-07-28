package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.GradleException
import org.gradle.api.Project
import spock.lang.Specification

class TestLoggerWrapperSpec extends Specification {

    def "wrapper delegates to sequential test logger if parallel theme is not applied"() {
        given:
            def extensionMock = Mock(TestLoggerExtension)
            extensionMock.theme >> ThemeType.STANDARD
            def projectMock = GroovyMock(Project) {
                getTestlogger() >> extensionMock
            }
        when:
            def wrapper = new TestLoggerWrapper(projectMock, 1)
        then:
            wrapper.testLoggerDelegate instanceof SequentialTestLogger
    }

    def "wrapper delegates to parallel test logger if parallel theme is applied"() {
        given:
            def extensionMock = Mock(TestLoggerExtension)
            extensionMock.theme >> ThemeType.STANDARD_PARALLEL
            def projectMock = GroovyMock(Project) {
                getTestlogger() >> extensionMock
            }
        when:
            def wrapper = new TestLoggerWrapper(projectMock, 1)
        then:
            wrapper.testLoggerDelegate instanceof ParallelTestLogger
    }

    def "wrapper throws exception if maxParallelForks is over 1 and parallel theme is not applied"() {
        given:
            def extensionMock = Mock(TestLoggerExtension)
            extensionMock.theme >> ThemeType.STANDARD
            def projectMock = GroovyMock(Project) {
                getTestlogger() >> extensionMock
            }
        when:
            new TestLoggerWrapper(projectMock, 2)
        then:
            def exception = thrown(GradleException)
            exception.message == "Parallel execution is not supported for theme type 'standard'. " +
                "Must be one of 'plain-parallel', 'standard-parallel', 'mocha-parallel'"
    }
}
