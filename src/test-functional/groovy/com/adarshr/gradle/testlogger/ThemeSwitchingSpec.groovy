package com.adarshr.gradle.testlogger

import spock.util.environment.OperatingSystem

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ThemeSwitchingSpec extends AbstractFunctionalSpec {

    def "log spock tests when plain theme is set"() {
        when:
            def result = runSingle("testlogger { theme 'plain' }")
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 3
            lines[0] == render('com.adarshr.test.SingleSpec')
            lines[1] == render('')
            lines[2] == render('  Test this is a single test PASSED')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when standard theme is set"() {
        when:
            def result = runSingle("testlogger { theme 'standard' }")
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 3
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.SingleSpec[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]this is a single test[erase-ahead,green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when mocha theme is set"() {
        when:
            def result = runSingle("testlogger { theme 'mocha' }")
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 3
            lines[0] == render('  [bold]com.adarshr.test.SingleSpec[/]')
            lines[1] == render('')
            lines[2] == render("    [erase-ahead,green]${symbol}[/] this is a single test")
        and:
            result.task(":test").outcome == SUCCESS
    }

    private static String getSymbol() {
        OperatingSystem.current.windows ? '√' : '✔'
    }
}
