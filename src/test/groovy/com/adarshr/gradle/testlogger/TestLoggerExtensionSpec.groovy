package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.logging.configuration.ConsoleOutput
import spock.lang.Specification
import spock.lang.Unroll


class TestLoggerExtensionSpec extends Specification {

    def projectMock = Mock(Project) {
        getGradle() >> Mock(Gradle) {
            getStartParameter() >> Mock(StartParameter) {
                getConsoleOutput() >> ConsoleOutput.Auto
            }
        }
    }

    def "test logger extension default properties"() {
        when:
            def extension = new TestLoggerExtension(projectMock, [:])
        then:
            extension.theme == ThemeType.STANDARD
            extension.showExceptions
            extension.slowThreshold == 2000
            extension.showSummary
            !extension.showStandardStreams
            extension.showPassedStandardStreams
            extension.showSkippedStandardStreams
            extension.showFailedStandardStreams
            extension.showPassed
            extension.showSkipped
            extension.showFailed
    }

    def "test logger extension properties can be overridden"() {
        given:
            def overrides = [
                theme: 'plain',
                showExceptions: 'false',
                slowThreshold: '4000',
                showSummary: 'false',
                showStandardStreams: 'true',
                showPassedStandardStreams: 'false',
                showSkippedStandardStreams: 'false',
                showFailedStandardStreams: 'false',
                showPassed: 'false',
                showSkipped: 'false',
                showFailed: 'false'
            ]
        when:
            def extension = new TestLoggerExtension(projectMock, overrides)
            extension.applyOverrides()
        then:
            extension.theme == ThemeType.PLAIN
            !extension.showExceptions
            extension.slowThreshold == 4000
            !extension.showSummary
            extension.showStandardStreams
            !extension.showPassedStandardStreams
            !extension.showSkippedStandardStreams
            !extension.showFailedStandardStreams
            !extension.showPassed
            !extension.showSkipped
            !extension.showFailed
    }

    @Unroll
    def "attempt to set theme when console type is plain is ignored"() {
        given:
            def projectMock = Mock(Project) {
                getGradle() >> Mock(Gradle) {
                    getStartParameter() >> Mock(StartParameter) {
                        getConsoleOutput() >> ConsoleOutput.Plain
                    }
                }
            }
        when:
            def extension = new TestLoggerExtension(projectMock, [:])
            //noinspection GroovyAssignabilityCheck
            extension.theme = theme
        then:
            extension.theme == ThemeType.PLAIN
        where:
            theme << [ThemeType.MOCHA_PARALLEL, 'mocha-parallel']
    }

    def "combine two test extension objects"() {
        given:
            def parent = new TestLoggerExtension(projectMock, [:])
            parent.theme = ThemeType.MOCHA
            parent.showPassed = false
            parent.showSkipped = true
            parent.slowThreshold = 10000
        and:
            def child = new TestLoggerExtension(projectMock, [:])
            child.theme = ThemeType.MOCHA_PARALLEL
            child.slowThreshold = 20000
            child.showSkipped = false
        when:
            def combined = child.combine(parent)
        then:
            combined.theme == ThemeType.MOCHA_PARALLEL
            combined.slowThreshold == 20000
            !combined.showPassed
            !combined.showSkipped
    }
}
