package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.logging.Logger
import org.gradle.api.logging.configuration.ConsoleOutput
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.testng.TestNGOptions
import spock.lang.Specification
import spock.lang.Unroll

class ThemeFactorySpec extends Specification {

    def extensionMock = Mock(TestLoggerExtension)
    def loggerMock = Mock(Logger)
    def testMock = Mock(Test) {
        getProject() >> Mock(Project) {
            getGradle() >> Mock(Gradle) {
                getStartParameter() >> Mock(StartParameter) {
                    getConsoleOutput() >> ConsoleOutput.Auto
                }
            }
            getLogger() >> loggerMock
        }
    }

    @Unroll
    def "loads theme #theme.simpleName when theme type is #themeType"() {
        given:
            extensionMock.theme >> themeType
        expect:
            ThemeFactory.getTheme(testMock, extensionMock).class == theme
        where:
            themeType                   | theme
            ThemeType.PLAIN             | PlainTheme
            ThemeType.PLAIN_PARALLEL    | PlainParallelTheme
            ThemeType.STANDARD          | StandardTheme
            ThemeType.STANDARD_PARALLEL | StandardParallelTheme
            ThemeType.MOCHA             | MochaTheme
            ThemeType.MOCHA_PARALLEL    | MochaParallelTheme
    }

    def "console type of plain overrides any theme configuration"() {
        given:
            extensionMock.theme >> ThemeType.MOCHA
            testMock = Mock(Test) {
                getName() >> 'fooTask'
                getProject() >> Mock(Project) {
                    getGradle() >> Mock(Gradle) {
                        getStartParameter() >> Mock(StartParameter) {
                            getConsoleOutput() >> ConsoleOutput.Plain
                        }
                    }
                    getLogger() >> loggerMock
                }
            }
        when:
            def theme = ThemeFactory.getTheme(testMock, extensionMock)
        then:
            theme.type == ThemeType.PLAIN
        and:
            1 * loggerMock.info('Test logger theme for task fooTask overridden from mocha to plain')
    }

    @Unroll
    def "parallel fallback theme is applied when maxParallelForks > 1 but a parallel theme is not used"() {
        given:
            extensionMock.theme >> extensionTheme
            testMock.maxParallelForks >> 2
            testMock.name >> 'fooTask'
        when:
            def theme = ThemeFactory.getTheme(testMock, extensionMock)
        then:
            theme.type == actualTheme
        and:
            1 * loggerMock.info("Test logger theme for task fooTask overridden " +
                "from ${extensionTheme.name} to ${actualTheme.name}")
        where:
            extensionTheme     | actualTheme
            ThemeType.PLAIN    | ThemeType.PLAIN_PARALLEL
            ThemeType.STANDARD | ThemeType.STANDARD_PARALLEL
            ThemeType.MOCHA    | ThemeType.MOCHA_PARALLEL
    }

    @Unroll
    def "parallel fallback theme is applied when TestNG parallel mode is on but a parallel theme is not used"() {
        given:
            extensionMock.theme >> extensionTheme
            testMock.maxParallelForks >> 2
            testMock.name >> 'fooTask'
            testMock.options >> Mock(TestNGOptions) {
                getParallel() >> 'methods'
            }
        when:
            def theme = ThemeFactory.getTheme(testMock, extensionMock)
        then:
            theme.type == actualTheme
        and:
            1 * loggerMock.info("Test logger theme for task fooTask overridden " +
                "from ${extensionTheme.name} to ${actualTheme.name}")
        where:
            extensionTheme     | actualTheme
            ThemeType.PLAIN    | ThemeType.PLAIN_PARALLEL
            ThemeType.STANDARD | ThemeType.STANDARD_PARALLEL
            ThemeType.MOCHA    | ThemeType.MOCHA_PARALLEL
    }
}
