package com.adarshr.test

import spock.lang.Specification

class SlowSpec extends Specification {

    def "this is a slow test"() {
        expect:
            sleep 3000
            true
    }
}
