package com.adarshr.test

import spock.lang.Ignore
import spock.lang.Narrative
import spock.lang.Specification
import spock.lang.Stepwise

@Stepwise
@Narrative('Test that calls System.exit from setupSpec method')
class SecondSpec extends Specification {

    def setupSpec() {
        println "${getClass().simpleName} - stdout setupSpec"
        System.err.println "${getClass().simpleName} - stderr setupSpec"

        System.exit(5)
    }

    def cleanupSpec() {
        println "${getClass().simpleName} - stdout cleanupSpec"
        System.err.println "${getClass().simpleName} - stderr cleanupSpec"
    }

    def setup() {
        println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stdout setup"
        System.err.println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stderr setup"
    }

    def cleanup() {
        println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stdout cleanup"
        System.err.println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stderr cleanup"
    }

    def "this test should pass"() {
        expect:
            println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stdout expect"
            System.err.println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stderr expect"
            1 == 1
    }

    def "this test should fail"() {
        expect:
            println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stdout expect"
            System.err.println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stderr expect"
            1 == 2
    }

    @Ignore
    def "this test should be skipped"() {
        expect:
            println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stdout expect"
            System.err.println "${getClass().simpleName} - ${specificationContext.currentFeature.name} - stderr expect"
            1 == 2
    }
}
