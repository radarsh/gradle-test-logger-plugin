package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import com.adarshr.gradle.testlogger.TestLoggerExtension
import com.adarshr.gradle.testlogger.TestResultWrapper
import com.adarshr.gradle.testlogger.theme.Theme
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.gradle.api.logging.Logger
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestOutputEvent
import org.gradle.api.tasks.testing.TestResult

import java.util.concurrent.ConcurrentHashMap

@CompileStatic
class TestLoggerAdapter implements TestLogger {

    protected final Theme theme
    protected final ConsoleLogger logger
    protected final OutputCollector outputCollector
    protected final TestLoggerExtension testLoggerExtension
    private final Deque<TestDescriptorWrapper> ancestors
    private final Map<String, TestDescriptorWrapper> descriptorCache = new ConcurrentHashMap<>()

    TestLoggerAdapter(Logger logger, TestLoggerExtension testLoggerExtension, Theme theme) {
        this.logger = new ConsoleLogger(logger, testLoggerExtension.logLevel)
        this.testLoggerExtension = testLoggerExtension
        this.theme = theme
        this.outputCollector = new OutputCollector()
        ancestors = new ArrayDeque<>()
    }

    @Override
    final void beforeSuite(TestDescriptor descriptor) {
        def wrappedDescriptor = checkAndWrap(descriptor)

        ancestors.push(wrappedDescriptor)

        if (isGradleSuite(wrappedDescriptor.name)) {
            ancestors.clear()
        }

        if (!descriptor.parent) {
            logger.logNewLine()
        }

        beforeSuite(wrappedDescriptor)
    }

    protected void beforeSuite(TestDescriptorWrapper descriptor) {
    }

    @Override
    final void afterSuite(TestDescriptor descriptor, TestResult result) {
        if (!ancestors.empty) {
            ancestors.pop()
        }
        if (!descriptor.parent) {
            ancestors.clear()
        }

        afterSuite(checkAndWrap(descriptor), wrap(result))

        descriptorCache.remove(id(descriptor))
    }

    protected void afterSuite(TestDescriptorWrapper descriptor, TestResultWrapper result) {
    }

    @Override
    final void beforeTest(TestDescriptor descriptor) {
        beforeTest(checkAndWrap(descriptor))
    }

    protected void beforeTest(TestDescriptorWrapper descriptor) {
    }

    @Override
    final void afterTest(TestDescriptor descriptor, TestResult result) {
        afterTest(checkAndWrap(descriptor), wrap(result))

        descriptorCache.remove(id(descriptor))
    }

    protected void afterTest(TestDescriptorWrapper descriptor, TestResultWrapper result) {
    }

    @Override
    void onOutput(TestDescriptor descriptor, TestOutputEvent outputEvent) {
        if (testLoggerExtension.showStandardStreams) {
            outputCollector.collect(checkAndWrap(descriptor), outputEvent.message)
        }
    }

    private TestDescriptorWrapper checkAndWrap(TestDescriptor descriptor) {
        descriptorCache.computeIfAbsent(id(descriptor)) { wrap(descriptor) }
    }

    @CompileDynamic
    private static String id(TestDescriptor descriptor) {
        descriptor.properties.id
    }

    protected TestDescriptorWrapper wrap(TestDescriptor descriptor) {
        new TestDescriptorWrapper(descriptor, testLoggerExtension, ancestors.toList().reverse())
    }

    protected TestResultWrapper wrap(TestResult result) {
        new TestResultWrapper(result, testLoggerExtension)
    }

    protected boolean isGradleSuite(String name) {
        name.startsWith('Gradle Test Executor') ||
            name.startsWith('Gradle suite') ||
            name.startsWith('Gradle test')
    }
}
