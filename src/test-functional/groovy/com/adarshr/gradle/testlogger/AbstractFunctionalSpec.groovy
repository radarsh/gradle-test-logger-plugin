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

    protected static final def START_MARKER = '[[ START ]]'
    protected static final def END_MARKER = '[[ END ]]'

    private AnsiTextRenderer ansi = new AnsiTextRenderer()

    protected List<String> getLoggerOutput(String text) {
        def lines = text.readLines()
        lines.subList(lines.indexOf(START_MARKER) + 1, lines.indexOf(END_MARKER))
    }

    protected String render(String ansiText) {
        ansi.render(ansiText)
    }

    protected BuildResult runMultiple(String project, String args = 'clean test') {
        runProject(new File("${TEST_ROOT}/${project}"), args)
    }

    protected BuildResult runSingle(String project, String buildFragment, String args = 'clean test') {
        def projectDir = new File(temporaryFolder.root, project)
        copyDirectoryToDirectory(new File("${TEST_ROOT}/${project}"), temporaryFolder.root)
        new File(projectDir, 'build.gradle') << buildFragment

        runProject(projectDir, args)
    }

    private BuildResult runProject(File projectDir, String args) {
        try {
            GradleRunner.create()
                .withGradleVersion('4.2')
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
