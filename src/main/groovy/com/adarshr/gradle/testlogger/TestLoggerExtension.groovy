package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import groovy.transform.CompileStatic
import org.gradle.api.tasks.testing.logging.TestLogging

import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL

@CompileStatic
@SuppressWarnings("GroovyUnusedDeclaration")
class TestLoggerExtension {

    ThemeType theme = STANDARD
    boolean showExceptions = true
    boolean showCauses = true
    boolean showStackTraces = true
    boolean showFullStackTraces = false
    long slowThreshold = 2000
    boolean showSummary = true
    boolean showStandardStreams = false
    boolean showPassedStandardStreams = true
    boolean showSkippedStandardStreams = true
    boolean showFailedStandardStreams = true
    boolean showPassed = true
    boolean showSkipped = true
    boolean showFailed = true
    boolean showSimpleNames = false

    private Set<String> configuredProperties = []

    TestLoggerExtension() {
    }

    private TestLoggerExtension(TestLoggerExtension source) {
        this.theme = source.theme
        this.showExceptions = source.showExceptions
        this.showCauses = source.showCauses
        this.showStackTraces = source.showStackTraces
        this.showFullStackTraces = source.showFullStackTraces
        this.slowThreshold = source.slowThreshold
        this.showSummary = source.showSummary
        this.showStandardStreams = source.showStandardStreams
        this.showPassedStandardStreams = source.showPassedStandardStreams
        this.showSkippedStandardStreams = source.showSkippedStandardStreams
        this.showFailedStandardStreams = source.showFailedStandardStreams
        this.showPassed = source.showPassed
        this.showSkipped = source.showSkipped
        this.showFailed = source.showFailed
        this.showSimpleNames = source.showSimpleNames
        this.configuredProperties = source.configuredProperties
    }

    void setTheme(String theme) {
        this.theme = ThemeType.fromName(theme)
        this.configuredProperties << 'theme'
    }

    void setTheme(ThemeType theme) {
        this.theme = theme
        this.configuredProperties << 'theme'
    }

    void setShowExceptions(boolean showExceptions) {
        this.showExceptions = showExceptions
        this.configuredProperties << 'showExceptions'
    }

    void setShowCauses(boolean showCauses) {
        this.showCauses = showCauses
        this.configuredProperties << 'showCauses'
    }

    void setShowStackTraces(boolean showStackTraces) {
        this.showStackTraces = showStackTraces
        this.configuredProperties << 'showStackTraces'
    }

    void setShowFullStackTraces(boolean showFullStackTraces) {
        this.showFullStackTraces = showFullStackTraces
        this.configuredProperties << 'showFullStackTraces'
    }

    void setSlowThreshold(long slowThreshold) {
        this.slowThreshold = slowThreshold
        this.configuredProperties << 'slowThreshold'
    }

    void setShowSummary(boolean showSummary) {
        this.showSummary = showSummary
        this.configuredProperties << 'showSummary'
    }

    void setShowStandardStreams(boolean showStandardStreams) {
        this.showStandardStreams = showStandardStreams
        this.configuredProperties << 'showStandardStreams'
    }

    void setShowPassedStandardStreams(boolean showPassedStandardStreams) {
        this.showPassedStandardStreams = showPassedStandardStreams
        this.configuredProperties << 'showPassedStandardStreams'
    }

    void setShowSkippedStandardStreams(boolean showSkippedStandardStreams) {
        this.showSkippedStandardStreams = showSkippedStandardStreams
        this.configuredProperties << 'showSkippedStandardStreams'
    }

    void setShowFailedStandardStreams(boolean showFailedStandardStreams) {
        this.showFailedStandardStreams = showFailedStandardStreams
        this.configuredProperties << 'showFailedStandardStreams'
    }

    void setShowPassed(boolean showPassed) {
        this.showPassed = showPassed
        this.configuredProperties << 'showPassed'
    }

    void setShowSkipped(boolean showSkipped) {
        this.showSkipped = showSkipped
        this.configuredProperties << 'showSkipped'
    }

    void setShowFailed(boolean showFailed) {
        this.showFailed = showFailed
        this.configuredProperties << 'showFailed'
    }

    void setShowSimpleNames(boolean showSimpleNames) {
        this.showSimpleNames = showSimpleNames
        this.configuredProperties << 'showSimpleNames'
    }

    TestLoggerExtension undecorate() {
        new TestLoggerExtension(this)
    }

    TestLoggerExtension reactTo(TestLogging testLogging) {
        if (!this.configuredProperties.contains('showStandardStreams')) {
            this.showStandardStreams = testLogging.showStandardStreams
            this.configuredProperties -= 'showStandardStreams'
        }
        if (!this.configuredProperties.contains('showExceptions')) {
            this.showExceptions = testLogging.showExceptions
            this.configuredProperties -= 'showExceptions'
        }
        if (!this.configuredProperties.contains('showCauses')) {
            this.showCauses = testLogging.showCauses
            this.configuredProperties -= 'showCauses'
        }
        if (!this.configuredProperties.contains('showStackTraces')) {
            this.showStackTraces = testLogging.showStackTraces
            this.configuredProperties -= 'showStackTraces'
        }
        if (!this.configuredProperties.contains('showFullStackTraces')) {
            this.showFullStackTraces = testLogging.showStackTraces && testLogging.exceptionFormat == FULL
            this.configuredProperties -= 'showFullStackTraces'
        }

        this
    }

    TestLoggerExtension combine(TestLoggerExtension another) {
        this.properties.findAll { Object property, Object value ->
            !this.configuredProperties.contains(property) && property != 'class'
        }.each { Object property, Object value ->
            this.setProperty(property as String, another.properties[property])
        }

        this.configuredProperties.clear()

        this
    }

    TestLoggerExtension applyOverrides(Map<String, String> overrides) {
        override(overrides, 'theme', ThemeType)
        override(overrides, 'showExceptions', Boolean)
        override(overrides, 'showCauses', Boolean)
        override(overrides, 'showStackTraces', Boolean)
        override(overrides, 'showFullStackTraces', Boolean)
        override(overrides, 'slowThreshold', Long)
        override(overrides, 'showSummary', Boolean)
        override(overrides, 'showStandardStreams', Boolean)
        override(overrides, 'showPassedStandardStreams', Boolean)
        override(overrides, 'showSkippedStandardStreams', Boolean)
        override(overrides, 'showFailedStandardStreams', Boolean)
        override(overrides, 'showPassed', Boolean)
        override(overrides, 'showSkipped', Boolean)
        override(overrides, 'showFailed', Boolean)
        override(overrides, 'showSimpleNames', Boolean)

        this
    }

    private void override(Map<String, String> overrides, String name, Class type) {
        if (overrides.containsKey(name)) {
            String method = Enum.isAssignableFrom(type) ? 'fromName' : 'valueOf'

            setProperty(name, type.invokeMethod(method, overrides[name]))
        }
    }
}
