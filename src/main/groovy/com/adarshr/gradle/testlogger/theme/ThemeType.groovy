package com.adarshr.gradle.testlogger.theme

import groovy.transform.CompileStatic
import org.gradle.api.GradleException

@CompileStatic
enum ThemeType {
    //@formatter:off
    //                  name                    parallel    themeClass              parallelFallback
    PLAIN(              'plain',                false,      PlainTheme,             'plain-parallel'),
    PLAIN_PARALLEL(     'plain-parallel',       true,       PlainParallelTheme,     null),
    STANDARD(           'standard',             false,      StandardTheme,          'standard-parallel'),
    STANDARD_PARALLEL(  'standard-parallel',    true,       StandardParallelTheme,  null),
    MOCHA(              'mocha',                false,      MochaTheme,             'mocha-parallel'),
    MOCHA_PARALLEL(     'mocha-parallel',       true,       MochaParallelTheme,     null)
    //@formatter:on

    final String name
    final boolean parallel
    final Class<? extends Theme> themeClass
    final String parallelFallback

    ThemeType(String name, boolean parallel, Class<? extends Theme> themeClass, String parallelFallback = null) {
        this.name = name
        this.parallel = parallel
        this.themeClass = themeClass
        this.parallelFallback = parallelFallback

        assert parallel || parallelFallback: 'A non-parallel theme must have a parallel fallback'
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
