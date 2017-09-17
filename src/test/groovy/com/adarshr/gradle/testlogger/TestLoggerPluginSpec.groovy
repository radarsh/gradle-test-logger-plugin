package com.adarshr.gradle.testlogger

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification

import static groovy.json.StringEscapeUtils.escapeJava
import static org.gradle.testkit.runner.TaskOutcome.FAILED

class TestLoggerPluginSpec extends Specification {

    /*
    - Test theme switching by configuration
    - Test theme switching based on console flag
     */
    private static final char E = '\u001B'

    def "log spock tests"() {
        when:
            def result = run('sample-spock-tests')
        then:
            def actual = getLoggerOutput(result.output, ':test', '6 tests completed, 2 failed, 2 skipped')
            def expected = """
                $E[1;93mcom.adarshr.test.FirstSpec$E[m
                $E[1m  Test $E[mthis test should pass$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[mthis test should pass$E[32m PASSED$E[m
                $E[1m  Test $E[mthis test should fail$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[mthis test should fail$E[31m FAILED$E[m
                $E[1m  Test $E[mthis test should be skipped$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[mthis test should be skipped$E[33m SKIPPED$E[m
                $E[1;93mcom.adarshr.test.SecondSpec$E[m
                $E[1m  Test $E[mthis test should pass$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[mthis test should pass$E[32m PASSED$E[m
                $E[1m  Test $E[mthis test should fail$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[mthis test should fail$E[31m FAILED$E[m
                $E[1m  Test $E[mthis test should be skipped$E[33m STARTED$E[m$E[F
                $E[2K$E[1m  Test $E[mthis test should be skipped$E[33m SKIPPED$E[m
            """.stripIndent()
            normalize(actual) == normalize(expected)
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
        loggerLines.join('\n')
    }

    private static String normalize(String input) {
        def escaped = escapeJava(input).replace('\\n', '\n').trim()
        def groupedBySpec = [:]

        escaped.readLines().each { line ->
            if (line.contains('Spec')) {
                groupedBySpec."${line}" = [line]
            } else {
                groupedBySpec[groupedBySpec.keySet().last()] << line
            }
        }

        groupedBySpec.sort { it.key }.values().flatten().join('\n')
    }
}
