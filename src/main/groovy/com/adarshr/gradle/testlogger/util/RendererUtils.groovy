package com.adarshr.gradle.testlogger.util

import groovy.transform.CompileStatic

@CompileStatic
class RendererUtils {

    static String escape(String text) {
        text?.replace('\u001B', '')?.replace('[', '\\[')?.replace(']', '\\]')
    }

    static String preserveAnsi(String text) {
        text?.replace('\u001B[', '\u001B\\[')
    }
}
