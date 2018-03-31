package com.adarshr.test

import spock.lang.Specification

class SingleSpec extends Specification {

    def setupSpec() {
        println 'stdout setupSpec'
        System.err.println 'stderr setupSpec'
    }

    def cleanupSpec() {
        println 'stdout cleanupSpec'
        System.err.println 'stderr cleanupSpec'
    }

    def setup() {
        println 'stdout setup'
        System.err.println 'stderr setup'
    }

    def cleanup() {
        println 'stdout cleanup'
        System.err.println 'stderr cleanup'
    }

    def "this is a single test"() {
        expect:
            println 'stdout expect'
            System.err.println 'stderr expect'
            true
    }
}
