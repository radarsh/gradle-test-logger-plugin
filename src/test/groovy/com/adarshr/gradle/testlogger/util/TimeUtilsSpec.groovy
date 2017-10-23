package com.adarshr.gradle.testlogger.util

import spock.lang.Specification
import spock.lang.Unroll

class TimeUtilsSpec extends Specification {

    @Unroll
    def "duration is #duration when millis is #millis"() {
        expect:
            duration == TimeUtils.humanDuration(millis)
        where:
            duration  | millis
            '0ms'     | 0
            '999ms'   | 999
            '1s'      | 1000
            '3.4s'    | 3403
            '4.9s'    | 4999
            '59.9s'   | 59999
            '1m'      | 60000
            '1m 2s'   | 62567
            '59m 59s' | 3599_999
            '1h'      | 3600_000
            '5h 32m'  | 19_920_000
            '23h 59m' | 86_364_000
    }
}
