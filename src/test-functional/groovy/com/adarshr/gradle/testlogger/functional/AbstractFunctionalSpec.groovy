package com.adarshr.gradle.testlogger.functional

import com.adarshr.gradle.testlogger.renderer.AnsiTextRenderer
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.UnexpectedBuildFailure
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static java.lang.System.lineSeparator
import static org.apache.commons.io.FileUtils.copyDirectoryToDirectory

@SuppressWarnings('GrMethodMayBeStatic')
abstract class AbstractFunctionalSpec extends Specification {

    private static final String TEST_ROOT = 'src/test-functional/resources'

    private static final String GRADLE_VERSION = '6.6.1'

    @Rule
    TemporaryFolder temporaryFolder

    private static final def START_MARKER = '__START__'
    private static final def END_MARKER = '__END__'
    private static final def SUMMARY_MARKER = '__SUMMARY__'
    private static final def SUITE_MARKER = '__SUITE='
    private static final def TEST_MARKER = '__TEST='
    private static final def SUITE_MARKER_REGEX = $/${SUITE_MARKER}(.*)__/$
    private static final def TEST_MARKER_REGEX = $/${TEST_MARKER}(.*)__/$

    private static final List<Map<String, String>> FILTER_PATTERNS = [
        [pattern: $/(?ms)${lineSeparator()}Unexpected exception thrown.*?${lineSeparator() * 2}/$, replacement: ''],
        [pattern: $/(?ms)> Task .*?${lineSeparator()}/$, replacement: '']
    ]

    private AnsiTextRenderer ansi = new AnsiTextRenderer()

    protected TestLoggerOutput getLoggerOutput(String text) {
        FILTER_PATTERNS.each { it ->
            text = text.replaceAll(it.pattern, it.replacement)
        }

        def allLines = text.readLines()
        def lines = allLines
            .subList(allLines.indexOf(START_MARKER) + 1, allLines.indexOf(SUMMARY_MARKER))
            .findAll { !it.startsWith(TEST_MARKER) }
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

    protected TestLoggerOutput getParallelLoggerOutput(String text) {
        def allLines = text.readLines()
        def lines = allLines
            .subList(allLines.indexOf(START_MARKER) + 1, allLines.indexOf(SUMMARY_MARKER))
            .findAll { !it.startsWith(SUITE_MARKER) }
        def summary = allLines.subList(allLines.indexOf(SUMMARY_MARKER) + 1, allLines.indexOf(END_MARKER))
        def map = new LinkedHashMap<String, List<String>>()

        lines.each { line ->
            if (line.startsWith(TEST_MARKER)) {
                map << [(line.replaceFirst(TEST_MARKER_REGEX, '$1')): []]
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
                .withGradleVersion(GRADLE_VERSION)
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
