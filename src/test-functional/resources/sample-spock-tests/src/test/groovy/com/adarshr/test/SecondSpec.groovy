package com.adarshr.test

import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
class SecondSpec extends Specification {

    def "this test should pass"() {
        expect:
            1 == 1
    }

    def "this test should fail"() {
        expect:
            1 == 2
    }

    @Ignore
    def "this test should be skipped"() {
        expect:
            1 == 2
    }
}
