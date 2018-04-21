package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.logging.configuration.ConsoleOutput
import spock.lang.Specification


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
    }

    def "test logger extension properties can be overridden"() {
        given:
            def overrides = [
                theme: 'plain',
                showExceptions: 'false',
                slowThreshold: '4000',
                showSummary: 'false',
                showStandardStreams: 'true'
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
    }
}
