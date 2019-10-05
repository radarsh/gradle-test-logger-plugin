package com.adarshr.gradle.testlogger.util

import groovy.transform.CompileStatic

@CompileStatic
class RendererUtils {

    static String escape(String text) {
        text?.replace('[', '\\[')?.replace(']', '\\]')
    }

    static String unescape(String text) {
        text?.replace('\\[', '[')?.replace('\\]', ']')
    }
}
