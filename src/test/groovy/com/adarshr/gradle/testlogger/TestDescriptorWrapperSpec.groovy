package com.adarshr.gradle.testlogger

import org.gradle.api.tasks.testing.TestDescriptor
import spock.lang.Specification


class TestDescriptorWrapperSpec extends Specification {

    def testDescriptorMock = GroovyMock(TestDescriptor)
    def testLoggerExtensionMock = Mock(TestLoggerExtension)
    def wrapper = new TestDescriptorWrapper(testDescriptorMock, testLoggerExtensionMock)

    def "className escapes special characters in delegate's className"() {
        given:
            testDescriptorMock.className >> 'ClassName [escaped]'
        expect:
            wrapper.className == 'ClassName \\[escaped\\]'
    }

    def "classDisplayName delegates to classDisplayName property if present"() {
        given:
            testDescriptorMock.properties >> [classDisplayName: 'class display name [escaped]']
            testDescriptorMock.className >> 'ClassName [escaped]'
        expect:
            wrapper.classDisplayName == 'class display name \\[escaped\\]'
    }

    def "classDisplayName falls back to className if classDisplayName property is missing"() {
        given:
            testDescriptorMock.properties >> [:]
            testDescriptorMock.className >> 'ClassName [escaped]'
        expect:
            wrapper.classDisplayName == 'ClassName \\[escaped\\]'
    }

    def "displayName delegates to displayName property if present"() {
        given:
            testDescriptorMock.properties >> [displayName: 'display name [escaped]']
            testDescriptorMock.name >> 'test name [escaped]'
        expect:
            wrapper.displayName == 'display name \\[escaped\\]'
    }

    def "displayName falls back to name if displayName property is missing"() {
        given:
            testDescriptorMock.properties >> [:]
            testDescriptorMock.name >> 'test name [escaped]'
        expect:
            wrapper.displayName == 'test name \\[escaped\\]'
    }

    def "get suite key"() {
        given:
            testDescriptorMock.className >> 'ClassName'
        expect:
            wrapper.suiteKey == 'ClassName:ClassName'
    }

    def "get test key"() {
        given:
            testDescriptorMock.className >> 'ClassName'
            testDescriptorMock.name >> 'test name'
        expect:
            wrapper.testKey == 'ClassName:test name'
    }
}
