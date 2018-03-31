package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.renderer.AnsiTextRenderer
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.apache.commons.io.FileUtils.copyDirectoryToDirectory

@SuppressWarnings('GrMethodMayBeStatic')
abstract class AbstractFunctionalSpec extends Specification {

    private static final String TEST_ROOT = 'src/test-functional/resources'

    @Rule
    TemporaryFolder temporaryFolder

    private static final def START_MARKER = '__START__'
    private static final def END_MARKER = '__END__'
    private static final def SUMMARY_MARKER = '__SUMMARY__'
    private static final def SUITE_MARKER = '__SUITE='
    private static final def SUITE_MARKER_REGEX = $/$SUITE_MARKER(.*)__/$

    private AnsiTextRenderer ansi = new AnsiTextRenderer()

    protected TestLoggerOutput getLoggerOutput(String text) {
        def allLines = text.readLines()
        def lines = allLines.subList(allLines.indexOf(START_MARKER) + 1, allLines.indexOf(SUMMARY_MARKER))
        def summary = allLines.subList(allLines.indexOf(SUMMARY_MARKER) + 1, allLines.indexOf(END_MARKER))
        def map = new LinkedHashMap<String, List<String>>()

        lines.each { line ->
            if (line.startsWith(SUITE_MARKER)) {
                map << [(line.replaceFirst(SUITE_MARKER_REGEX, '$1')): []]
            } else {
                map.values().last() << line
            }
        }

        new TestLoggerOutput(lines: map.sort().values().flatten(), summary: summary)
    }

    protected String render(String ansiText) {
        ansi.render(ansiText)
    }

    protected BuildResult run(String project, String args) {
        run(project, '', args)
    }

    protected BuildResult run(String project, String buildFragment, String args) {
        def projectDir = new File(temporaryFolder.root, project)
        def buildFile = new File(projectDir, 'build.gradle')
        copyDirectoryToDirectory(new File("${TEST_ROOT}/${project}"), temporaryFolder.root)
        buildFile << new File(TEST_ROOT, 'test-marker.gradle').text
        buildFile << buildFragment

        runProject(projectDir, args)
    }

    private BuildResult runProject(File projectDir, String args) {
        try {
            GradleRunner.create()
                .withGradleVersion('4.6')
                .withProjectDir(projectDir)
                .withPluginClasspath()
                .withDebug(true)
                .withArguments(args.split(' '))
                .forwardOutput()
                .build()
        } catch (UnexpectedBuildFailure e) {
            e.buildResult
        }
    }
}
