package com.adarshr.gradle.testlogger.theme

import groovy.transform.CompileStatic
import org.gradle.api.GradleException

@CompileStatic
enum ThemeType {
    PLAIN('plain', false),
    PLAIN_PARALLEL('plain-parallel', true),
    STANDARD('standard', false),
    STANDARD_PARALLEL('standard-parallel', true),
    MOCHA('mocha', false),
    MOCHA_PARALLEL('mocha-parallel', true)

    final String name
    final boolean parallel

    ThemeType(String name, boolean parallel) {
        this.name = name
        this.parallel = parallel
    }

    static ThemeType fromName(String name) {
        def themeType = values().find {
            it.name == name
        }

        if (!themeType) {
            throw new GradleException("Unknown theme '${name}'. Must be one of ${allThemeNames}")
        }

        themeType
    }

    static String getAllThemeNames() {
        values().collect { ThemeType themeType ->
            "'${themeType.name}'"
        }.join(', ')
    }

    static String getParallelThemeNames() {
        values().findAll {
            it.parallel
        }.collect {
            "'${it.name}'"
        }.join(', ')
    }
}
