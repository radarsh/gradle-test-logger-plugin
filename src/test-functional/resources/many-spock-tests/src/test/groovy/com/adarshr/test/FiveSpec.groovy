package com.adarshr.test

import spock.lang.Specification

class FiveSpec extends Specification {

    def setupSpec() {
        println "${getClass().simpleName} - stdout setupSpec"
        System.err.println "${getClass().simpleName} - stderr setupSpec"
    }

    def cleanupSpec() {
        println "${getClass().simpleName} - stdout cleanupSpec"
        System.err.println "${getClass().simpleName} - stderr cleanupSpec"
    }

    def setup() {
        println "${getClass().simpleName} - stdout setup"
        System.err.println "${getClass().simpleName} - stderr setup"
        sleep Math.round(Math.random() * 500)
    }

    def cleanup() {
        println "${getClass().simpleName} - stdout cleanup"
        System.err.println "${getClass().simpleName} - stderr cleanup"
    }

    def "this is test one"() {
        expect:
            println "${getClass().simpleName} - stdout expect"
            System.err.println "${getClass().simpleName} - stderr expect"
            true
    }

    def "this is test two"() {
        expect:
            println "${getClass().simpleName} - stdout expect"
            System.err.println "${getClass().simpleName} - stderr expect"
            true
    }

    def "this is test three"() {
        expect:
            println "${getClass().simpleName} - stdout expect"
            System.err.println "${getClass().simpleName} - stderr expect"
            true
    }

    def "this is test four"() {
        expect:
            println "${getClass().simpleName} - stdout expect"
            System.err.println "${getClass().simpleName} - stderr expect"
            true
    }
}
