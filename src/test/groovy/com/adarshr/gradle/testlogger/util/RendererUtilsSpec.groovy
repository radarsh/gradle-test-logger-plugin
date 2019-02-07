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

    @Unroll
    def "preserve ansi #text"() {
        expect:
            RendererUtils.preserveAnsi(text) == expected
        where:
            text              | expected
            null              | null
            ''                | ''
            '[do not escape]' | '[do not escape]'
            '\u001B[0mANSI'   | '\u001B\\[0mANSI'
    }
}
