package com.adarshr.gradle.testlogger

import org.gradle.api.tasks.testing.TestDescriptor
import spock.lang.Specification
import spock.lang.Unroll


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

    @Unroll
    def "classDisplayName is #expected when classDisplayName property = #classDisplayName, className = #className and showSimpleNames = #showSimpleNames"() {
        given:
            testDescriptorMock.properties >> [classDisplayName: classDisplayName]
            testDescriptorMock.className >> className
        and:
            testLoggerExtensionMock.showSimpleNames >> showSimpleNames
        expect:
            wrapper.classDisplayName == expected
        where:
            classDisplayName   | className                  | showSimpleNames | expected
            'Test'             | 'com.adarshr.Test'         | false           | 'com.adarshr.Test'
            'A test'           | 'com.adarshr.Test'         | false           | 'A test'
            ''                 | 'com.adarshr.Test'         | false           | 'com.adarshr.Test'
            null               | 'com.adarshr.Test'         | false           | 'com.adarshr.Test'
            'Two'              | 'com.adarshr.Test$One$Two' | false           | 'com.adarshr.Test$One$Two'
            'com.adarshr.Test' | 'com.adarshr.Test'         | false           | 'com.adarshr.Test'
            'Test'             | 'com.adarshr.Test'         | true            | 'Test'
            'A test'           | 'com.adarshr.Test'         | true            | 'A test'
            ''                 | 'com.adarshr.Test'         | true            | 'Test'
            null               | 'com.adarshr.Test'         | true            | 'Test'
            'Two'              | 'com.adarshr.Test$One$Two' | true            | 'Two'
            'com.adarshr.Test' | 'com.adarshr.Test'         | true            | 'Test'
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
