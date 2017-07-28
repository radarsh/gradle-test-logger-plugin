package com.adarshr.gradle.plugins

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static groovy.json.StringEscapeUtils.escapeJava
import static org.gradle.testkit.runner.TaskOutcome.FAILED

class TestLoggerPluginSpec extends Specification {

    private static final char E = '\u001B'

    def "log spock tests"() {
        when:
            def result = run('sample-spock-tests')
        then:
            def actual = getLoggerOutput(result.output, ':test', '6 tests completed, 2 failed, 2 skipped')
            def expected = """
                $E[1;93mcom.adarshr.test.FirstSpec$E[m
                $E[1m  Test $E[39mthis test should pass$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[39mthis test should pass$E[32m PASSED$E[m
                $E[1m  Test $E[39mthis test should fail$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[39mthis test should fail$E[31m FAILED$E[m
                $E[1m  Test $E[39mthis test should be skipped$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[39mthis test should be skipped$E[33m SKIPPED$E[m
                $E[1;93mcom.adarshr.test.SecondSpec$E[m
                $E[1m  Test $E[39mthis test should pass$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[39mthis test should pass$E[32m PASSED$E[m
                $E[1m  Test $E[39mthis test should fail$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[39mthis test should fail$E[31m FAILED$E[m
                $E[1m  Test $E[39mthis test should be skipped$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[39mthis test should be skipped$E[33m SKIPPED$E[m
            """.stripIndent()
            escapeJava(actual).replace('\\n', '\n') == escapeJava(expected).replace('\\n', '\n')
        and:
            result.task(":test").outcome == FAILED
    }

    private static BuildResult run(String project) {
        GradleRunner.create()
            .withGradleVersion('4.0.2')
            .withProjectDir(new File("src/test/resources/${project}"))
            .withPluginClasspath()
            .withDebug(true)
            .withArguments('clean', 'test')
            .forwardOutput()
            .buildAndFail()
    }

    private static String getLoggerOutput(String text, String startMarker, String endMarker) {
        def lines = text.readLines()
        def loggerLines = lines.subList(lines.indexOf(startMarker) + 1, lines.indexOf(endMarker) - 1)
        "\n${loggerLines.join('\n')}\n"
    }
}
