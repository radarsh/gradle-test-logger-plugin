package com.adarshr.gradle.testlogger.theme

abstract class AbstractTheme implements Theme {

    @SuppressWarnings("GrMethodMayBeStatic")
    String escape(String text) {
        text.replace('[', '\\[').replace(']', '\\]')
    }
}
