package com.adarshr.gradle.plugins

import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.FAILED

class TestLoggerPluginSpec extends Specification {

    def "can run tests"() {
        when:
            def result = GradleRunner.create()
                .withGradleVersion('4.0.2')
                .withProjectDir(new File('src/test/resources/sample-spock-tests'))
                .withPluginClasspath()
                .withArguments('clean', 'test')
                .forwardOutput()
                .buildAndFail()
        then:
            result.task(":test").outcome == FAILED
    }
}
