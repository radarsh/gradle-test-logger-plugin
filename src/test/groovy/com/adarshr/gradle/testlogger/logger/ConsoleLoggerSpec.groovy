package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.renderer.AnsiTextRenderer
import org.gradle.api.logging.Logger
import spock.lang.Specification

import static org.gradle.api.logging.LogLevel.LIFECYCLE

class ConsoleLoggerSpec extends Specification {

    def loggerMock = Mock(Logger)

    def "log"() {
        given:
            GroovySpy(AnsiTextRenderer, global: true)
            def ansiTextRendererMock = Mock(AnsiTextRenderer)
            new AnsiTextRenderer() >> ansiTextRendererMock
            ansiTextRendererMock.render('text to be logged') >> 'rendered ansi text'
        and:
            def consoleLogger = new ConsoleLogger(loggerMock)
        when:
            consoleLogger.log('text to be logged')
        then:
            1 * loggerMock.log(LIFECYCLE, 'rendered ansi text')
    }
}
