package com.adarshr.gradle.testlogger.util

import spock.lang.Specification
import spock.lang.Unroll


class RendererUtilsSpec extends Specification {

    @Unroll
    def "escape #text"() {
        expect:
            RendererUtils.escape(text) == expected
        where:
            text         | expected
            null         | null
            ''           | ''
            '[escape]'   | '\\[escape\\]'
            '\u001Btext' | 'text'
    }
}
