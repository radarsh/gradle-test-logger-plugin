package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.renderer.AnsiTextRenderer
import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ThemeSwitchingSpec extends Specification {

    private static final def START_MARKER = '[[ START ]]'
    private static final def END_MARKER = '[[ END ]]'

    private static final def MARKERS = """
        test {
            doFirst {
                logger.lifecycle '$START_MARKER'
            }
            doLast {
                logger.lifecycle '$END_MARKER'
            }
        }
    """

    @Rule
    TemporaryFolder temporaryFolder

    def "log spock tests when plain theme is set"() {
        when:
            def result = run('single-spock-test', """
                testlogger {
                    theme 'plain'
                }
                
                $MARKERS
            """)
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 2
            actualLines[0] == 'com.adarshr.test.SingleSpec'
            actualLines[1] == '  Test this is a single test PASSED'
        and:
            result.task(":test").outcome == SUCCESS
    }

    def ansi = new AnsiTextRenderer()

    def "log spock tests when standard theme is set"() {
        when:
            def result = run('single-spock-test', """
                testlogger {
                    theme 'standard'
                }
                
                $MARKERS
            """)
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 3
            actualLines[0] == ansi.render('[bold,bright-yellow]com.adarshr.test.SingleSpec[/]')
            actualLines[1] == ansi.render('[bold]  Test [/]this is a single test[yellow] STARTED[/][cursor-up-line]')
            actualLines[2] == ansi.render('[erase-line,bold]  Test [/]this is a single test[green] PASSED[/]')
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

    private static List<String> getLoggerOutput(String text) {
        def lines = text.readLines()
        lines.subList(lines.indexOf(START_MARKER) + 1, lines.indexOf(END_MARKER))
    }
}
