package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper
import com.adarshr.gradle.testlogger.theme.Theme
import groovy.transform.CompileStatic
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestOutputEvent
import org.gradle.api.tasks.testing.TestResult

@CompileStatic
class TestLoggerAdapter implements TestLogger {

    protected final Theme theme
    protected final ConsoleLogger logger
    protected final OutputCollector outputCollector
    private final TestLoggerExtension testLoggerExtension

    TestLoggerAdapter(Logger logger, TestLoggerExtension testLoggerExtension, Theme theme) {
        this.logger = new ConsoleLogger(logger, testLoggerExtension.logLevel)
        this.testLoggerExtension = testLoggerExtension
        this.theme = theme
        this.outputCollector = new OutputCollector()
    }

    @Override
    final void beforeSuite(TestDescriptor descriptor) {
        beforeSuite(wrap(descriptor))
    }

    protected void beforeSuite(TestDescriptorWrapper descriptor) {
    }

    @Override
    final void afterSuite(TestDescriptor descriptor, TestResult result) {
        afterSuite(wrap(descriptor), wrap(result))
    }

    protected void afterSuite(TestDescriptorWrapper descriptor, TestResultWrapper result) {
    }

    @Override
    final void beforeTest(TestDescriptor descriptor) {
        beforeTest(wrap(descriptor))
    }

    protected void beforeTest(TestDescriptorWrapper descriptor) {
    }

    @Override
    final void afterTest(TestDescriptor descriptor, TestResult result) {
        afterTest(wrap(descriptor), wrap(result))
    }

    protected void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
    }

    @Override
    void onOutput(TestDescriptor descriptor, TestOutputEvent outputEvent) {
        if (testLoggerExtension.showStandardStreams) {
            outputCollector.collect(wrap(descriptor), outputEvent.message)
        }
    }

    private TestDescriptorWrapper wrap(TestDescriptor descriptor) {
        new TestDescriptorWrapper(descriptor, testLoggerExtension)
    }

    private TestResultWrapper wrap(TestResult result) {
        new TestResultWrapper(result, testLoggerExtension)
    }
}
