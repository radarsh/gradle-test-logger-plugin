package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.testng.TestNGOptions
import spock.lang.Specification

class TestLoggerWrapperSpec extends Specification {

    def extensionMock = Mock(TestLoggerExtension)
    def projectMock = GroovyMock(Project) {
        getTestlogger() >> extensionMock
    }
    def testMock = Mock(Test)

    def "wrapper delegates to sequential test logger if parallel theme is not applied"() {
        given:
            extensionMock.theme >> ThemeType.STANDARD
            testMock.maxParallelForks >> 1
        when:
            def wrapper = new TestLoggerWrapper(projectMock, testMock)
        then:
            wrapper.testLoggerDelegate instanceof SequentialTestLogger
    }

    def "wrapper delegates to parallel test logger if parallel theme is applied"() {
        given:
            extensionMock.theme >> ThemeType.STANDARD_PARALLEL
            testMock.maxParallelForks >> 1
        when:
            def wrapper = new TestLoggerWrapper(projectMock, testMock)
        then:
            wrapper.testLoggerDelegate instanceof ParallelTestLogger
    }

    def "wrapper throws exception if maxParallelForks is over 1 and parallel theme is not applied"() {
        given:
            extensionMock.theme >> ThemeType.STANDARD
            testMock.maxParallelForks >> 2
        when:
            new TestLoggerWrapper(projectMock, testMock)
        then:
            def exception = thrown(GradleException)
            exception.message == "Parallel execution is not supported for theme type 'standard'. " +
                "Must be one of 'plain-parallel', 'standard-parallel', 'mocha-parallel'"
    }

    def "wrapper throws exception if TestNG parallel mode is used but parallel theme is not applied"() {
        given:
            extensionMock.theme >> ThemeType.STANDARD
            testMock.options >> Mock(TestNGOptions) { getParallel() >> 'methods' }
        when:
            new TestLoggerWrapper(projectMock, testMock)
        then:
            def exception = thrown(GradleException)
            exception.message == "Parallel execution is not supported for theme type 'standard'. " +
                "Must be one of 'plain-parallel', 'standard-parallel', 'mocha-parallel'"
    }
}
