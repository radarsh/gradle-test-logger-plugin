package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import groovy.transform.CompileStatic
import org.gradle.api.Project
import org.gradle.api.logging.configuration.ConsoleOutput

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

    private final ConsoleOutput consoleType
    private final Map<String, String> overrides
    private Set<String> configuredProperties = []

    TestLoggerExtension(Project project, Map<String, String> overrides) {
        this.consoleType = project.gradle.startParameter.consoleOutput
        this.theme = project.gradle.startParameter.consoleOutput == Plain ? PLAIN : this.theme
        this.overrides = overrides
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
        this.consoleType = source.consoleType
        this.overrides = source.overrides
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

    TestLoggerExtension combine(TestLoggerExtension another) {
        def copyOfThis = new TestLoggerExtension(this)

        copyOfThis.properties.findAll { Object property, Object value ->
            !copyOfThis.configuredProperties.contains(property) && property != 'class'
        }.each { Object property, Object value ->
            copyOfThis.setProperty(property as String, another.properties[property])
        }

        copyOfThis.configuredProperties.clear()

        copyOfThis
    }

    void applyOverrides() {
        override('theme', ThemeType)
        override('showExceptions', Boolean)
        override('slowThreshold', Long)
        override('showSummary', Boolean)
        override('showStandardStreams', Boolean)
        override('showPassedStandardStreams', Boolean)
        override('showSkippedStandardStreams', Boolean)
        override('showFailedStandardStreams', Boolean)
        override('showPassed', Boolean)
        override('showSkipped', Boolean)
        override('showFailed', Boolean)
    }

    private void override(String name, Class type) {
        if (overrides.containsKey(name)) {
            String method = Enum.isAssignableFrom(type) ? 'fromName' : 'valueOf'

            setProperty(name, type.invokeMethod(method, overrides[name]))
            this.configuredProperties << name
        }
    }
}
