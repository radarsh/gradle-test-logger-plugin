package com.adarshr.gradle.testlogger.util


class RendererUtils {

    static String escape(String text) {
        text?.replace('\u001B', '')?.replace('[', '\\[')?.replace(']', '\\]')
    }
}
