package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.logging.configuration.ConsoleOutput
import org.gradle.api.tasks.testing.logging.TestLogging

import static com.adarshr.gradle.testlogger.theme.ThemeType.PLAIN
import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static org.gradle.api.logging.configuration.ConsoleOutput.Plain

@CompileStatic
@SuppressWarnings("GroovyUnusedDeclaration")
class TestLoggerExtension {

    ThemeType theme = STANDARD
    boolean showExceptions = true
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

    private final ConsoleOutput consoleType
    private Set<String> configuredProperties = []

    TestLoggerExtension(Project project) {
        this.consoleType = project.gradle.startParameter.consoleOutput
        this.theme = project.gradle.startParameter.consoleOutput == Plain ? PLAIN : this.theme
    }

    private TestLoggerExtension(TestLoggerExtension source) {
        this.theme = source.theme
        this.showExceptions = source.showExceptions
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
        this.consoleType = source.consoleType
        this.configuredProperties = source.configuredProperties
    }

    void setTheme(String theme) {
        if (consoleType == Plain) {
            return
        }

        this.theme = ThemeType.fromName(theme)
        this.configuredProperties << 'theme'
    }

    void setTheme(ThemeType theme) {
        if (consoleType == Plain) {
            return
        }

        this.theme = theme
        this.configuredProperties << 'theme'
    }

    void setShowExceptions(boolean showExceptions) {
        this.showExceptions = showExceptions
        this.configuredProperties << 'showExceptions'
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
