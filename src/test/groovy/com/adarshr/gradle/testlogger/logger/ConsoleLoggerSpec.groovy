package com.adarshr.gradle.testlogger.logger


import com.adarshr.gradle.testlogger.renderer.TextRenderer
import org.gradle.api.logging.Logger
import spock.lang.Specification

import static org.gradle.api.logging.LogLevel.LIFECYCLE

class ConsoleLoggerSpec extends Specification {

    def loggerMock = Mock(Logger)
    def textRendererMock = Mock(TextRenderer)
    ConsoleLogger consoleLogger

    def setup() {
        consoleLogger = new ConsoleLogger(loggerMock, textRendererMock)
    }

    def "log"() {
        given:
            textRendererMock.render('text to be logged') >> 'rendered ansi text'
        when:
            consoleLogger.log('text to be logged')
        then:
            1 * loggerMock.log(LIFECYCLE, 'rendered ansi text')
    }

    def "does not log empty strings"() {
        when:
            consoleLogger.log('')
        then:
            0 * loggerMock._
            0 * textRendererMock._
    }

    def "log new line"() {
        when:
            consoleLogger.logNewLine()
        then:
            1 * loggerMock.log(LIFECYCLE, '')
    }
}
