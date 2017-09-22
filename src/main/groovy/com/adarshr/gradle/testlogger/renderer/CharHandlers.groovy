package com.adarshr.gradle.testlogger.renderer

class CharHandlers {

    private static final CharHandler backslash = { char ch, RenderingContext context ->
        context.escaped = true
    }

    private static final CharHandler leftBracket = { char ch, RenderingContext context ->
        if (context.escaped) {
            context.ansi.a(ch)
            context.escaped = false
            return
        }

        context.beginTag()
    }

    private static final CharHandler rightBracket = { char ch, RenderingContext context ->
        if (context.escaped) {
            context.ansi.a(ch)
            context.escaped = false
            return
        }

        context.endTag()
    }

    public static final Map<Character, CharHandler> HANDLERS = [
        ('[' as char): leftBracket,
        (']' as char): rightBracket,
        ('\\' as char): backslash,
    ]
}
