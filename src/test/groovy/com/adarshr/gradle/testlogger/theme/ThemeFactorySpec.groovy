package com.adarshr.gradle.testlogger.theme

import com.adarshr.gradle.testlogger.TestLoggerExtension
import spock.lang.Specification
import spock.lang.Unroll

class ThemeFactorySpec extends Specification {

    def extensionMock = Mock(TestLoggerExtension)

    @Unroll
    def "loads theme #theme.simpleName when theme type is #themeType"() {
        given:
            extensionMock.theme >> themeType
        expect:
            ThemeFactory.getTheme(extensionMock).class == theme
        where:
            themeType                   | theme
            ThemeType.PLAIN             | PlainTheme
            ThemeType.PLAIN_PARALLEL    | PlainParallelTheme
            ThemeType.STANDARD          | StandardTheme
            ThemeType.STANDARD_PARALLEL | StandardParallelTheme
            ThemeType.MOCHA             | MochaTheme
            ThemeType.MOCHA_PARALLEL    | MochaParallelTheme
    }
}
