package com.adarshr.gradle.testlogger

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ThemeSwitchingSpec extends AbstractFunctionalSpec {

    def "log spock tests when plain theme is set"() {
        when:
            def result = run('single-spock-test', "testlogger { theme 'plain' }")
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 2
            actualLines[0] == render('com.adarshr.test.SingleSpec')
            actualLines[1] == render('  Test this is a single test PASSED')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when standard theme is set"() {
        when:
            def result = run('single-spock-test', "testlogger { theme 'standard' }")
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 3
            actualLines[0] == render('[bold,bright-yellow]com.adarshr.test.SingleSpec[/]')
            actualLines[1] == render('')
            actualLines[2] == render('[bold]  Test [/]this is a single test[erase-ahead,green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when mocha theme is set"() {
        when:
            def result = run('single-spock-test', "testlogger { theme 'mocha' }")
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 3
            actualLines[0] == render('  [bold]com.adarshr.test.SingleSpec[/]')
            actualLines[1] == render('')
            actualLines[2] == render('    [erase-ahead,green]✔[/] this is a single test')
        and:
            result.task(":test").outcome == SUCCESS
    }
}
