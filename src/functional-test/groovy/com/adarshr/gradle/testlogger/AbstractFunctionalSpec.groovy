package com.adarshr.gradle.testlogger

import com.adarshr.gradle.testlogger.renderer.AnsiTextRenderer
import org.apache.commons.io.FileUtils
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification


@SuppressWarnings('GrMethodMayBeStatic')
class AbstractFunctionalSpec extends Specification {

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

    protected BuildResult run(String project) {
        GradleRunner.create()
            .withGradleVersion('4.2')
            .withProjectDir(new File("src/functional-test/resources/${project}"))
            .withPluginClasspath()
            .withDebug(true)
            .withArguments('clean', 'test')
            .forwardOutput()
            .buildAndFail()
    }


    protected BuildResult run(String project, String buildFragment) {
        def projectDir = new File(temporaryFolder.root, project)
        FileUtils.copyDirectoryToDirectory(new File("src/functional-test/resources/${project}"), temporaryFolder.root)
        def buildFile = new File(projectDir, 'build.gradle')
        buildFile << buildFragment

        GradleRunner.create()
            .withGradleVersion('4.2')
            .withProjectDir(projectDir)
            .withPluginClasspath()
            .withDebug(true)
            .withArguments('clean', 'test')
            .forwardOutput()
            .build()
    }
}
