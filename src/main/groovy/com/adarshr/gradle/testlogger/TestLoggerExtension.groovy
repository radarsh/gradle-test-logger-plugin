package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.theme.ThemeType
import groovy.transform.CompileStatic
import groovy.transform.PackageScope
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel
import org.gradle.api.provider.Property
import org.gradle.api.provider.ProviderFactory
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.logging.TestLogEvent

import static com.adarshr.gradle.testlogger.theme.ThemeType.STANDARD
import static org.gradle.api.logging.LogLevel.LIFECYCLE
import static org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import static org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_ERROR
import static org.gradle.api.tasks.testing.logging.TestLogEvent.STANDARD_OUT

@CompileStatic
@SuppressWarnings('unused')
class TestLoggerExtension extends TestLoggerExtensionProperties {

    private final Property<ThemeType> theme
    private final Property<LogLevel> logLevel
    private final Property<Boolean> showExceptions
    private final Property<Boolean> showCauses
    private final Property<Boolean> showStackTraces
    private final Property<Boolean> showFullStackTraces
    private final Property<Long> slowThreshold
    private final Property<Boolean> showSummary
    private final Property<Boolean> showStandardStreams
    private final Property<Boolean> showPassedStandardStreams
    private final Property<Boolean> showSkippedStandardStreams
    private final Property<Boolean> showFailedStandardStreams
    private final Property<Boolean> showPassed
    private final Property<Boolean> showSkipped
    private final Property<Boolean> showFailed
    private final Property<Boolean> showSimpleNames

    private final SetProperty<TestLogEvent> originalTestLoggingEvents
    private final TestLoggerExtension projectExtension
    private final ProviderFactory providers
    private final Test test

    TestLoggerExtension(Project project, Test test = null) {
        this.theme = project.objects.property(ThemeType)
        this.logLevel = project.objects.property(LogLevel)
        this.showExceptions = project.objects.property(Boolean)
        this.showCauses = project.objects.property(Boolean)
        this.showStackTraces = project.objects.property(Boolean)
        this.showFullStackTraces = project.objects.property(Boolean)
        this.slowThreshold = project.objects.property(Long)
        this.showSummary = project.objects.property(Boolean)
        this.showStandardStreams = project.objects.property(Boolean)
        this.showPassedStandardStreams = project.objects.property(Boolean)
        this.showSkippedStandardStreams = project.objects.property(Boolean)
        this.showFailedStandardStreams = project.objects.property(Boolean)
        this.showPassed = project.objects.property(Boolean)
        this.showSkipped = project.objects.property(Boolean)
        this.showFailed = project.objects.property(Boolean)
        this.showSimpleNames = project.objects.property(Boolean)

        this.originalTestLoggingEvents = project.objects.setProperty(TestLogEvent)
        this.projectExtension = project.extensions.findByType(TestLoggerExtension)
        this.providers = project.providers
        this.test = test
    }

    ThemeType getTheme() {
        providers.systemProperty('testlogger.theme')
            .forUseAtConfigurationTime()
            .map { ThemeType.fromName(it) }
            .orElse(theme)
            .orElse(projectExtension.@theme)
            .getOrElse(STANDARD)
    }

    LogLevel getLogLevel() {
        providers.systemProperty('testlogger.logLevel')
            .forUseAtConfigurationTime()
            .map { LogLevel.valueOf(it.toUpperCase()) }
            .orElse(logLevel)
            .orElse(projectExtension.@logLevel)
            .getOrElse(LIFECYCLE)
    }

    Boolean getShowExceptions() {
        providers.systemProperty('testlogger.showExceptions')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showExceptions)
            .orElse(projectExtension.@showExceptions)
            .orElse(providers.provider {
                test.testLogging.showExceptions
            })
            .getOrElse(true)
    }

    Boolean getShowCauses() {
        providers.systemProperty('testlogger.showCauses')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showCauses)
            .orElse(projectExtension.@showCauses)
            .orElse(providers.provider {
                test.testLogging.showCauses
            })
            .getOrElse(true)
    }

    Boolean getShowStackTraces() {
        providers.systemProperty('testlogger.showStackTraces')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showStackTraces)
            .orElse(projectExtension.@showStackTraces)
            .orElse(providers.provider {
                test.testLogging.showStackTraces
            })
            .getOrElse(true)
    }

    Boolean getShowFullStackTraces() {
        providers.systemProperty('testlogger.showFullStackTraces')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showFullStackTraces)
            .orElse(projectExtension.@showFullStackTraces)
            .orElse(providers.provider {
                test.testLogging.showStackTraces && test.testLogging.exceptionFormat == FULL ?: null
            })
            .getOrElse(false)
    }

    Long getSlowThreshold() {
        providers.systemProperty('testlogger.slowThreshold')
            .forUseAtConfigurationTime()
            .map { Long.valueOf(it) }
            .orElse(slowThreshold)
            .orElse(projectExtension.@slowThreshold)
            .getOrElse(2000L)
    }

    Boolean getShowSummary() {
        providers.systemProperty('testlogger.showSummary')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showSummary)
            .orElse(projectExtension.@showSummary)
            .getOrElse(true)
    }

    Boolean getShowStandardStreams() {
        providers.systemProperty('testlogger.showStandardStreams')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showStandardStreams)
            .orElse(projectExtension.@showStandardStreams)
            .orElse(providers.provider {
                originalTestLoggingEvents.get().contains(STANDARD_OUT) &&
                    originalTestLoggingEvents.get().contains(STANDARD_ERROR)
            })
            .getOrElse(false)
    }

    Boolean getShowPassedStandardStreams() {
        providers.systemProperty('testlogger.showPassedStandardStreams')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showPassedStandardStreams)
            .orElse(projectExtension.@showPassedStandardStreams)
            .getOrElse(true)
    }

    Boolean getShowSkippedStandardStreams() {
        providers.systemProperty('testlogger.showSkippedStandardStreams')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showSkippedStandardStreams)
            .orElse(projectExtension.@showSkippedStandardStreams)
            .getOrElse(true)
    }

    Boolean getShowFailedStandardStreams() {
        providers.systemProperty('testlogger.showFailedStandardStreams')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showFailedStandardStreams)
            .orElse(projectExtension.@showFailedStandardStreams)
            .getOrElse(true)
    }

    Boolean getShowPassed() {
        providers.systemProperty('testlogger.showPassed')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showPassed)
            .orElse(projectExtension.@showPassed)
            .getOrElse(true)
    }

    Boolean getShowSkipped() {
        providers.systemProperty('testlogger.showSkipped')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showSkipped)
            .orElse(projectExtension.@showSkipped)
            .getOrElse(true)
    }

    Boolean getShowFailed() {
        providers.systemProperty('testlogger.showFailed')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showFailed)
            .orElse(projectExtension.@showFailed)
            .getOrElse(true)
    }

    Boolean getShowSimpleNames() {
        providers.systemProperty('testlogger.showSimpleNames')
            .forUseAtConfigurationTime()
            .map { Boolean.valueOf(it) }
            .orElse(showSimpleNames)
            .orElse(projectExtension.@showSimpleNames)
            .getOrElse(false)
    }

    @PackageScope
    void setOriginalTestLoggingEvents(Set<TestLogEvent> events) {
        this.originalTestLoggingEvents.value(events).finalizeValue()
    }

    /**
     * Test logger theme. Defaults to 'standard'.
     *
     * @param theme the theme.
     */
    void setTheme(String theme) {
        this.theme.set(ThemeType.fromName(theme))
    }

    @Override
    void setTheme(ThemeType theme) {
        this.theme.set(theme)
    }

    /**
     * Log level used to print the messages. Defaults to 'lifecycle'.
     * @param logLevel the log level.
     */
    void setLogLevel(String logLevel) {
        this.logLevel.set(LogLevel.valueOf(logLevel.toUpperCase()))
    }

    @Override
    void setLogLevel(LogLevel logLevel) {
        this.logLevel.set(logLevel)
    }

    @Override
    void setShowExceptions(Boolean showExceptions) {
        this.showExceptions.set(showExceptions)
    }

    @Override
    void setShowCauses(Boolean showCauses) {
        this.showCauses.set(showCauses)
    }

    @Override
    void setShowStackTraces(Boolean showStackTraces) {
        this.showStackTraces.set(showStackTraces)
    }

    @Override
    void setShowFullStackTraces(Boolean showFullStackTraces) {
        this.showFullStackTraces.set(showFullStackTraces)
    }

    @Override
    void setSlowThreshold(Long slowThreshold) {
        this.slowThreshold.set(slowThreshold)
    }

    @Override
    void setShowSummary(Boolean showSummary) {
        this.showSummary.set(showSummary)
    }

    @Override
    void setShowStandardStreams(Boolean showStandardStreams) {
        this.showStandardStreams.set(showStandardStreams)
    }

    @Override
    void setShowPassedStandardStreams(Boolean showPassedStandardStreams) {
        this.showPassedStandardStreams.set(showPassedStandardStreams)
    }

    @Override
    void setShowSkippedStandardStreams(Boolean showSkippedStandardStreams) {
        this.showSkippedStandardStreams.set(showSkippedStandardStreams)
    }

    @Override
    void setShowFailedStandardStreams(Boolean showFailedStandardStreams) {
        this.showFailedStandardStreams.set(showFailedStandardStreams)
    }

    @Override
    void setShowPassed(Boolean showPassed) {
        this.showPassed.set(showPassed)
    }

    @Override
    void setShowSkipped(Boolean showSkipped) {
        this.showSkipped.set(showSkipped)
    }

    @Override
    void setShowFailed(Boolean showFailed) {
        this.showFailed.set(showFailed)
    }

    @Override
    void setShowSimpleNames(Boolean showSimpleNames) {
        this.showSimpleNames.set(showSimpleNames)
    }
}
