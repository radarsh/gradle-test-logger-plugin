package com.adarshr.gradle.testlogger

import org.gradle.api.tasks.testing.TestDescriptor
import spock.lang.Specification

class TestDescriptorWrapperSpec extends Specification {

    def testDescriptorMock = GroovyMock(TestDescriptor)
    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    def wrapper = new TestDescriptorWrapper(testDescriptorMock, testLoggerExtensionMock, [
        Mock(TestDescriptorWrapper) { displayName >> 'great grand parent' },
        Mock(TestDescriptorWrapper) { displayName >> 'grand parent' },
        Mock(TestDescriptorWrapper) { displayName >> 'parent' },
    ])

    def "id delegates to id property"() {
        given:
            testDescriptorMock.properties >> [id: 'identifier']
        expect:
            wrapper.id == 'identifier'
    }

    def "trail is constructed based on ancestors"() {
        given:
            testDescriptorMock.displayName >> 'child'
        expect:
            wrapper.trail == 'great grand parent > grand parent > parent'
    }

    def "depth is constructed based on ancestors"() {
        given:
            testDescriptorMock.displayName >> 'child'
        expect:
            wrapper.depth == 3
    }

    def "display name is escaped"() {
        given:
            testDescriptorMock.displayName >> 'display name [escaped]'
        expect:
            wrapper.displayName == 'display name \\[escaped\\]'
    }

    def "display name of top level descriptor is #displayName if actual display name #actualDisplayName, className is #className and showSimpleNames is false"() {
        given:
            wrapper = new TestDescriptorWrapper(testDescriptorMock, testLoggerExtensionMock, [])
            testDescriptorMock.displayName >> actualDisplayName
            testDescriptorMock.className >> className
            testLoggerExtensionMock.showSimpleNames >> false
        expect:
            wrapper.displayName == displayName
        where:
            className                  | actualDisplayName  | displayName
            'com.adarshr.Test'         | 'Test'             | 'com.adarshr.Test'
            'com.adarshr.Test'         | 'Bar'              | 'Bar'
            'com.adarshr.Test'         | 'com.adarshr.Test' | 'com.adarshr.Test'
            'com.adarshr.Test$One$Two' | 'Two'              | 'com.adarshr.Test$One$Two'
    }

    def "display name of top level descriptor is #displayName when actual display name is #actualDisplayName, className is #className and showSimpleNames is true"() {
        given:
            wrapper = new TestDescriptorWrapper(testDescriptorMock, testLoggerExtensionMock, [])
            testDescriptorMock.displayName >> actualDisplayName
            testDescriptorMock.className >> className
            testLoggerExtensionMock.showSimpleNames >> true
        expect:
            wrapper.displayName == displayName
        where:
            className                  | actualDisplayName | displayName
            'com.adarshr.Test'         | 'Test'            | 'Test'
            'com.adarshr.Test'         | 'Bar'             | 'Bar'
            'com.adarshr.Test$One$Two' | 'Two'             | 'Two'
            'com.adarshr.Test$One$Two' | 'Two'             | 'Two'
            'com.adarshr.Test$One$Two' | 'Bar'             | 'Bar'
    }
}
