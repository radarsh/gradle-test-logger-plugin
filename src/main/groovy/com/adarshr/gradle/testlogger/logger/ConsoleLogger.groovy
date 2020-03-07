package com.adarshr.gradle.testlogger.logger

import com.adarshr.gradle.testlogger.renderer.AnsiTextRenderer
import com.adarshr.gradle.testlogger.renderer.TextRenderer
import groovy.transform.CompileStatic
import org.gradle.api.logging.LogLevel
import org.gradle.api.logging.Logger

import static com.adarshr.gradle.testlogger.util.RendererUtils.unescape

@CompileStatic
class ConsoleLogger {

    private final Logger logger
    private final LogLevel level
    private final TextRenderer renderer

    ConsoleLogger(Logger logger, LogLevel level, TextRenderer renderer = new AnsiTextRenderer()) {
        this.logger = logger
        this.level = level
        this.renderer = renderer
    }

    void log(String text) {
        if (text) {
            logger.log(level, unescape(renderer.render(text)))
        }
    }

    void logNewLine() {
        logger.log(level, '')
    }
}
