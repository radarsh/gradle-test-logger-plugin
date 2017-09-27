package com.adarshr.gradle.testlogger.theme

import spock.lang.Specification
import spock.lang.Unroll

class ThemeFactorySpec extends Specification {

    @Unroll
    def "loads theme #theme when theme type is #themeType"() {
        expect:
            ThemeFactory.loadTheme(themeType).class == theme
        where:
            themeType          | theme
            ThemeType.PLAIN    | PlainTheme
            ThemeType.STANDARD | StandardTheme
            ThemeType.MOCHA    | MochaTheme
    }
}
