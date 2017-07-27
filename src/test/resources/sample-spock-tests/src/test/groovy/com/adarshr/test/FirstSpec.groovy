package com.adarshr.test

import spock.lang.Ignore
import spock.lang.Specification

class FirstSpec extends Specification {

    def "this test should pass"() {
        expect:
            true
    }

    def "this test should fail"() {
        expect:
            false
    }

    @Ignore
    def "this test should be skipped"() {
        expect:
            true
    }
}
