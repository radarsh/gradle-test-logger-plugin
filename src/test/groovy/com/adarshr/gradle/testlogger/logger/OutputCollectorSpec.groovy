package com.adarshr.gradle.testlogger.logger


import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import groovy.transform.Canonical
import org.gradle.api.tasks.testing.TestDescriptor
import spock.lang.Specification

class OutputCollectorSpec extends Specification {

    def "output collector groups output by descriptor and removes them accordingly"() {
        given:
            def collector = new OutputCollector()
            def suite = new TestDescriptorWrapper(new Descriptor(name: 'ClassName', className: 'ClassName'), null)
            def test = new TestDescriptorWrapper(new Descriptor(name: 'test', className: 'ClassName'), null)
        when:
            collector.collect(suite, 'Suite line 1\n')
            collector.collect(suite, 'Suite line 2\n')
            collector.collect(test, 'Test line 1\n')
            collector.collect(test, 'Test line 2\n')
        then:
            collector.removeTestOutput(test) == 'Test line 1\nTest line 2\n'
            !collector.removeTestOutput(test)
            collector.removeSuiteOutput(suite) == 'Suite line 1\nSuite line 2\n'
            !collector.removeSuiteOutput(suite)
    }

    @Canonical
    private static class Descriptor implements TestDescriptor {
        String name
        String displayName
        String className
        boolean composite
        Descriptor parent
    }
}
