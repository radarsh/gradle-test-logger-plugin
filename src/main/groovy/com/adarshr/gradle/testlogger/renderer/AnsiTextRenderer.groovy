package com.adarshr.gradle.testlogger.renderer

import static com.adarshr.gradle.testlogger.renderer.CharHandlers.HANDLERS

class AnsiTextRenderer implements TextRenderer {

    @Override
    String render(String input) {
        RenderingContext context = new RenderingContext()

        input.chars.each { ch ->
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
