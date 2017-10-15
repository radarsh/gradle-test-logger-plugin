package com.adarshr.gradle.testlogger

import spock.util.environment.OperatingSystem

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ThemeSwitchingSpec extends AbstractFunctionalSpec {

    def "log spock tests when plain theme is set"() {
        when:
            def result = run('single-spock-test', "testlogger { theme 'plain' }", 'clean test')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 4
            lines[0] == render('com.adarshr.test.SingleSpec')
            lines[1] == render('')
            lines[2] == render('  Test this is a single test PASSED')
            lines[3] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when standard theme is set"() {
        when:
            def result = run('single-spock-test', "testlogger { theme 'standard' }", 'clean test')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 4
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.SingleSpec[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]this is a single test[erase-ahead,green] PASSED[/]')
            lines[3] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when mocha theme is set"() {
        when:
            def result = run('single-spock-test', "testlogger { theme 'mocha' }", 'clean test')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 4
            lines[0] == render('  [bold]com.adarshr.test.SingleSpec[/]')
            lines[1] == render('')
            lines[2] == render("    [erase-ahead,green]${symbol}[/] this is a single test")
            lines[3] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    private static String getSymbol() {
        OperatingSystem.current.windows ? '√' : '✔'
    }
}
