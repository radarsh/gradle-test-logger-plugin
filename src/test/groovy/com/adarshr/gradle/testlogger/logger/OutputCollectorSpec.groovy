package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.TestDescriptorWrapper
import spock.lang.Specification

class OutputCollectorSpec extends Specification {

    def "output collector collects output by descriptor ID and removes them accordingly"() {
        given:
            def collector = new OutputCollector()
            def descriptor1Mock = Mock(TestDescriptorWrapper) { id >> 'desc-1' }
            def descriptor2Mock = Mock(TestDescriptorWrapper) { id >> 'desc-2' }
            def descriptor3Mock = Mock(TestDescriptorWrapper) { id >> 'desc-3' }
        when:
            collector.collect(descriptor1Mock, 'Suite line 1\n')
            collector.collect(descriptor1Mock, 'Suite line 2\n')
            collector.collect(descriptor2Mock, 'Test line 1\n')
            collector.collect(descriptor3Mock, 'Test line 2\n')
        then:
            collector.pop(descriptor2Mock) == 'Test line 1\n'
            !collector.pop(descriptor2Mock)
            collector.pop(descriptor3Mock) == 'Test line 2\n'
            !collector.pop(descriptor3Mock)
            collector.pop(descriptor1Mock) == 'Suite line 1\nSuite line 2\n'
            !collector.pop(descriptor1Mock)
    }
}
