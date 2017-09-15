package com.adarshr.gradle.testlogger

import org.apache.commons.io.FileUtils
import org.fusesource.jansi.Ansi
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.fusesource.jansi.Ansi.Erase.ALL
import static org.fusesource.jansi.Ansi.ansi
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ThemeSwitchingSpec extends Specification {

    private static final def START_MARKER = '[[ START ]]'
    private static final def END_MARKER = '[[ END ]]'

    @Rule
    TemporaryFolder temporaryFolder

    def "log spock tests when plain theme is set"() {
        when:
            def result = run('single-spock-test', """
                testlogger {
                    theme 'plain'
                }
                
                test {
                    doFirst {
                        logger.lifecycle '$START_MARKER'
                    }
                    doLast {
                        logger.lifecycle '$END_MARKER'
                    }
                }
            """)
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 2
            actualLines[0] == 'com.adarshr.test.SingleSpec'
            actualLines[1] == '  Test this is a single test PASSED'
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when standard theme is set"() {
        when:
            def result = run('single-spock-test', """
                testlogger {
                    theme 'standard'
                }
                
                test {
                    doFirst {
                        logger.lifecycle '$START_MARKER'
                    }
                    doLast {
                        logger.lifecycle '$END_MARKER'
                    }
                }
            """)
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 3
            actualLines[0] == ansi().bold().fgBrightYellow().a('com.adarshr.test.SingleSpec').reset().toString()
            actualLines[0] == ansi().bold().render('@|yellow com.adarshr.test.SingleSpec|@').toString()  //fgBrightYellow().a('com.adarshr.test.SingleSpec').reset().toString()
            actualLines[1] == ansi().bold().a('  Test ').reset().a('this is a single test').fgYellow().a(' STARTED').reset().cursorUpLine().toString()
            actualLines[2] == ansi().eraseLine(ALL).bold().a('  Test ').reset().a('this is a single test').fgGreen().a(' PASSED').reset().toString()
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
