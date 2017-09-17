package com.adarshr.gradle.testlogger.renderer

import org.fusesource.jansi.Ansi

import static org.fusesource.jansi.Ansi.Erase.ALL
import static org.fusesource.jansi.Ansi.ansi

class RenderingContext {

    private static final def TAG_MAPPING = [
        bold: { Ansi ansi -> ansi.bold() },
        red: { Ansi ansi -> ansi.fgRed() },
        'bright-red': { Ansi ansi -> ansi.fgBrightRed() },
        green: { Ansi ansi -> ansi.fgGreen() },
        yellow: { Ansi ansi -> ansi.fgYellow() },
        'bright-yellow': { Ansi ansi -> ansi.fgBrightYellow() },
        'cursor-up-line': { Ansi ansi -> ansi.cursorUpLine() },
        'erase-line': { Ansi ansi -> ansi.eraseLine(ALL) },
        '/': { Ansi ansi -> ansi.reset() }
    ]

    Ansi ansi = ansi()
    StringBuilder tagBuilder = new StringBuilder()
    boolean inTag = false
    boolean escaped = false

    void beginTag() {
        inTag = true
    }

    void endTag() {
        def tag = tagBuilder.toString()
        def tags = tag.split(',')
        tags.each {
            if (TAG_MAPPING.containsKey(it)) {
                def mapping = TAG_MAPPING[it]

                mapping.call(ansi)
            } else {
                ansi.a("[${it}]")
            }
        }

        tagBuilder = new StringBuilder()
        inTag = false
    }
}
