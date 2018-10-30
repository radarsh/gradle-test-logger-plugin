package com.adarshr.gradle.testlogger.renderer

import groovy.transform.CompileStatic

import static com.adarshr.gradle.testlogger.renderer.CharHandlers.HANDLERS

@CompileStatic
class AnsiTextRenderer implements TextRenderer {

    @Override
    String render(String input) {
        RenderingContext context = new RenderingContext()

        input.chars.each { char ch ->
            if (HANDLERS.containsKey(ch)) {
                HANDLERS[ch].handle(ch, context)
                return
            }

            if (context.inTag) {
                context.tag << ch
                return
            }

            context << ch
        }

        context
    }
}
