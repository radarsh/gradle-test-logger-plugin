package com.adarshr.gradle.testlogger

import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static groovy.json.StringEscapeUtils.escapeJava
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ThemeSwitchingSpec extends Specification {

    @Rule
    TemporaryFolder temporaryFolder

    def "log spock tests when plain theme is set"() {
        when:
            def result = run('single-spock-test', '''
                testlogger {
                    theme 'plain'
                }
            ''')
        then:
            def actual = getLoggerOutput(result.output, ':test', '3 actionable tasks: 3 executed')
            normalize(actual) == normalize('''
            com.adarshr.test.SingleSpec
              Test this is a single test PASSED
            '''.stripIndent())
        and:
            result.task(":test").outcome == SUCCESS
    }

    private BuildResult run(String project, String buildFragment) {
        def projectDir = new File(temporaryFolder.root, project)
        FileUtils.copyDirectoryToDirectory(new File("src/test/resources/${project}"), temporaryFolder.root)
        def buildFile = new File(projectDir, 'build.gradle')
        buildFile << buildFragment

        GradleRunner.create()
            .withGradleVersion('4.0.2')
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withDebug(true)
            .withArguments('clean', 'test')
            .forwardOutput()
            .build()
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
