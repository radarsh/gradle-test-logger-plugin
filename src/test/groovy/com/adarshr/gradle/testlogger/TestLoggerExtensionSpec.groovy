package com.adarshr.gradle.testlogger


import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification
import spock.lang.Unroll

import static com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA
import static com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA_PARALLEL
import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static org.gradle.api.logging.LogLevel.INFO
import static org.gradle.api.logging.LogLevel.LIFECYCLE
import static org.gradle.api.logging.LogLevel.QUIET
import static org.gradle.api.logging.LogLevel.WARN
import static org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import static org.gradle.api.tasks.testing.logging.TestExceptionFormat.SHORT

class TestLoggerExtensionSpec extends Specification {

    Test test
    TestLoggerExtension projectExtension
    TestLoggerExtension testExtension
    Project project = ProjectBuilder.builder().build()

    void setup() {
        projectExtension = project.extensions.create('testlogger', TestLoggerExtension, project)
        test = project.tasks.create('test', Test)
        test.configure { test ->
            testExtension = test.extensions.create('testlogger', TestLoggerExtension, project, test)
        }
    }


    @Unroll
    def "test logger theme property value is #expected when #scenario"() {
        given:
            def property = 'theme'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected       | scenario
            null           | null               | null                  | STANDARD       | 'default settings used'
            null           | 'mocha'            | null                  | MOCHA          | 'test extension configured'
            null           | MOCHA              | null                  | MOCHA          | 'test extension configured using ThemeType'
            null           | null               | 'mocha-parallel'      | MOCHA_PARALLEL | 'project extension configured'
            null           | null               | MOCHA_PARALLEL        | MOCHA_PARALLEL | 'project extension configured using ThemeType'
            null           | 'mocha'            | 'mocha-parallel'      | MOCHA          | 'both extensions configured'
            'mocha'        | 'plain'            | 'mocha-parallel'      | MOCHA          | 'system property provided'
    }

    @Unroll
    def "test logger logLevel property value is #expected when #scenario"() {
        given:
            def property = 'logLevel'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected  | scenario
            null           | null               | null                  | LIFECYCLE | 'default settings used'
            null           | 'warn'             | null                  | WARN      | 'test extension configured'
            null           | WARN               | null                  | WARN      | 'test extension configured using LogLevel'
            null           | null               | 'info'                | INFO      | 'project extension configured'
            null           | null               | INFO                  | INFO      | 'project extension configured using LogLevel'
            null           | 'warn'             | 'info'                | WARN      | 'both extensions configured'
            'quiet'        | 'warn'             | 'info'                | QUIET     | 'system property provided'
    }

    @Unroll
    def "test logger showExceptions property value is #expected when #scenario"() {
        given:
            def property = 'showExceptions'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        and:
            test.testLogging.showExceptions = testLoggingShowExceptions
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | testLoggingShowExceptions | expected | scenario
            null           | null               | null                  | true                      | true     | 'default settings used'
            null           | false              | null                  | true                      | false    | 'test extension configured'
            null           | null               | false                 | true                      | false    | 'project extension configured'
            null           | false              | true                  | true                      | false    | 'both extensions configured'
            null           | null               | null                  | false                     | false    | 'only testLogging configured'
            'false'        | true               | true                  | true                      | false    | 'system property provided'
    }

    @Unroll
    def "test logger showCauses property value is #expected when #scenario"() {
        given:
            def property = 'showCauses'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        and:
            test.testLogging.showCauses = testLoggingShowCauses
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | testLoggingShowCauses | expected | scenario
            null           | null               | null                  | true                  | true     | 'default settings used'
            null           | false              | null                  | true                  | false    | 'test extension configured'
            null           | null               | false                 | true                  | false    | 'project extension configured'
            null           | false              | true                  | true                  | false    | 'both extensions configured'
            null           | null               | null                  | false                 | false    | 'only testLogging configured'
            'false'        | true               | true                  | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showStackTraces property value is #expected when #scenario"() {
        given:
            def property = 'showStackTraces'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        and:
            test.testLogging.showStackTraces = testLoggingShowStackTraces
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | testLoggingShowStackTraces | expected | scenario
            null           | null               | null                  | true                       | true     | 'default settings used'
            null           | false              | null                  | true                       | false    | 'test extension configured'
            null           | null               | false                 | true                       | false    | 'project extension configured'
            null           | false              | true                  | true                       | false    | 'both extensions configured'
            null           | null               | null                  | false                      | false    | 'only testLogging configured'
            'false'        | true               | true                  | true                       | false    | 'system property provided'
    }

    @Unroll
    def "test logger showFullStackTraces property value is #expected when #scenario"() {
        given:
            def property = 'showFullStackTraces'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        and:
            test.testLogging.showStackTraces = testLoggingShowStackTraces
            test.testLogging.exceptionFormat = testLoggingExceptionFormat
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | testLoggingShowStackTraces | testLoggingExceptionFormat | expected | scenario
            null           | null               | null                  | true                       | SHORT                      | false    | 'default settings used'
            null           | null               | null                  | true                       | FULL                       | true     | 'only testLogging FULL exceptionFormat configured'
            null           | null               | null                  | false                      | FULL                       | false    | 'only testLogging showStackTraces is false'
            null           | true               | null                  | false                      | SHORT                      | true     | 'test extension configured'
            null           | null               | true                  | false                      | SHORT                      | true     | 'project extension configured'
            null           | true               | true                  | false                      | SHORT                      | true     | 'both extensions configured'
            'false'        | true               | true                  | true                       | SHORT                      | false    | 'system property provided'
    }

    @Unroll
    def "test logger slowThreshold property value is #expected when #scenario"() {
        given:
            def property = 'slowThreshold'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | 2000     | 'default settings used'
            null           | 1000               | null                  | 1000     | 'test extension configured'
            null           | null               | 5000                  | 5000     | 'project extension configured'
            null           | 3000               | 4000                  | 3000     | 'both extensions configured'
            '7000'         | 3000               | 4000                  | 7000     | 'system property provided'
    }

    @Unroll
    def "test logger showSummary property value is #expected when #scenario"() {
        given:
            def property = 'showSummary'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | true     | 'default settings used'
            null           | false              | null                  | false    | 'test extension configured'
            null           | null               | false                 | false    | 'project extension configured'
            null           | false              | true                  | false    | 'both extensions configured'
            'false'        | true               | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showStandardStreams property value is #expected when #scenario"() {
        given:
            def property = 'showStandardStreams'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        and:
            test.testLogging.showStandardStreams = testLoggingShowStandardStreams
            testExtension.originalTestLoggingEvents = test.testLogging.events
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | testLoggingShowStandardStreams | expected | scenario
            null           | null               | null                  | false                          | false    | 'default settings used'
            null           | true               | null                  | false                          | true     | 'test extension configured'
            null           | null               | true                  | false                          | true     | 'project extension configured'
            null           | true               | false                 | true                           | true     | 'both extensions configured'
            null           | null               | null                  | true                           | true     | 'only testLogging configured'
            'true'         | false              | false                 | false                          | true     | 'system property provided'
    }

    @Unroll
    def "test logger showPassedStandardStreams property value is #expected when #scenario"() {
        given:
            def property = 'showPassedStandardStreams'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | true     | 'default settings used'
            null           | false              | null                  | false    | 'test extension configured'
            null           | null               | false                 | false    | 'project extension configured'
            null           | false              | true                  | false    | 'both extensions configured'
            'false'        | true               | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showSkippedStandardStreams property value is #expected when #scenario"() {
        given:
            def property = 'showSkippedStandardStreams'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | true     | 'default settings used'
            null           | false              | null                  | false    | 'test extension configured'
            null           | null               | false                 | false    | 'project extension configured'
            null           | false              | true                  | false    | 'both extensions configured'
            'false'        | true               | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showFailedStandardStreams property value is #expected when #scenario"() {
        given:
            def property = 'showFailedStandardStreams'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | true     | 'default settings used'
            null           | false              | null                  | false    | 'test extension configured'
            null           | null               | false                 | false    | 'project extension configured'
            null           | false              | true                  | false    | 'both extensions configured'
            'false'        | true               | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showPassed property value is #expected when #scenario"() {
        given:
            def property = 'showPassed'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | true     | 'default settings used'
            null           | false              | null                  | false    | 'test extension configured'
            null           | null               | false                 | false    | 'project extension configured'
            null           | false              | true                  | false    | 'both extensions configured'
            'false'        | true               | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showSkipped property value is #expected when #scenario"() {
        given:
            def property = 'showSkipped'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | true     | 'default settings used'
            null           | false              | null                  | false    | 'test extension configured'
            null           | null               | false                 | false    | 'project extension configured'
            null           | false              | true                  | false    | 'both extensions configured'
            'false'        | true               | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showFailed property value is #expected when #scenario"() {
        given:
            def property = 'showFailed'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | true     | 'default settings used'
            null           | false              | null                  | false    | 'test extension configured'
            null           | null               | false                 | false    | 'project extension configured'
            null           | false              | true                  | false    | 'both extensions configured'
            'false'        | true               | true                  | false    | 'system property provided'
    }

    @Unroll
    def "test logger showSimpleNames property value is #expected when #scenario"() {
        given:
            def property = 'showSimpleNames'
            setupExtensions(property, systemProperty, projectExtensionValue, testExtensionValue)
        expect:
            testExtension."${property}" == expected
        cleanup:
            cleanupExtensions(property, systemProperty)
        where:
            systemProperty | testExtensionValue | projectExtensionValue | expected | scenario
            null           | null               | null                  | false    | 'default settings used'
            null           | true               | null                  | true     | 'test extension configured'
            null           | null               | true                  | true     | 'project extension configured'
            null           | true               | false                 | true     | 'both extensions configured'
            'true'         | false              | false                 | true     | 'system property provided'
    }

    void setupExtensions(String property, String systemProperty, Object projectExtensionValue, Object testExtensionValue) {
        if (systemProperty != null) {
            System.setProperty("testlogger.${property}", systemProperty)
        }
        if (projectExtensionValue != null) {
            projectExtension."${property}" = projectExtensionValue
        }
        if (testExtensionValue != null) {
            testExtension."${property}" = testExtensionValue
        }
    }

    void cleanupExtensions(String property, String systemProperty) {
        if (systemProperty != null) {
            System.clearProperty("testlogger.${property}")
        }
    }
}
