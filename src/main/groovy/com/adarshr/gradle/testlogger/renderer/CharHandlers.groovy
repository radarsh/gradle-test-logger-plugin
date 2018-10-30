package com.adarshr.gradle.testlogger.renderer

import groovy.transform.CompileStatic

@CompileStatic
class CharHandlers {

    private static final CharHandler backslash = { char ch, RenderingContext context ->
        context.escaped = true
    } as CharHandler

    private static final CharHandler leftBracket = { char ch, RenderingContext context ->
        if (context.escaped) {
            context << ch
            context.escaped = false
            return
        }

        context.beginTag()
    } as CharHandler

    private static final CharHandler rightBracket = { char ch, RenderingContext context ->
        if (context.escaped) {
            context << ch
            context.escaped = false
            return
        }

        context.endTag()
    } as CharHandler

    public static final Map<Character, CharHandler> HANDLERS = [
        ('[' as char): leftBracket,
        (']' as char): rightBracket,
        ('\\' as char): backslash,
    ]
}
