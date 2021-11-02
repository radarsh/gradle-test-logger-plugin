package com.adarshr.gradle.testlogger.functional


import spock.util.environment.OperatingSystem

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ThemeSwitchingSpec extends AbstractFunctionalSpec {

    def "log spock tests when plain theme is set"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        theme 'plain'
                        slowThreshold 5000 
                    }
                ''',
                'clean test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('')
            lines[1] == render('com.adarshr.test.SingleSpec')
            lines[2] == render('')
            lines[3] == render('  Test this is a single test PASSED')

        and:
            result.task(":test").outcome == SUCCESS
    }

    def "fallback to plain theme when --console plain is specified"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        theme 'standard'
                        slowThreshold 5000 
                    }
                ''',
                'clean test --console plain'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('')
            lines[1] == render('com.adarshr.test.SingleSpec')
            lines[2] == render('')
            lines[3] == render('  Test this is a single test PASSED')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when standard theme is set"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        theme 'standard'
                        slowThreshold 5000 
                    }
                ''',
                'clean test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.SingleSpec[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this is a single test[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spock tests when mocha theme is set"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        theme 'mocha'
                        slowThreshold 5000 
                    }
                ''',
                'clean test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('')
            lines[1] == render('  [erase-ahead,default]com.adarshr.test.SingleSpec[/]')
            lines[2] == render('')
            lines[3] == render("    [erase-ahead][green]${symbol}[grey] this is a single test[/]")
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "theme can be overridden using system property"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        theme 'mocha'
                        slowThreshold 5000
                    }
                    test.doLast { 
                        System.clearProperty('testlogger.theme') 
                    }
                ''',
                'clean test -Dtestlogger.theme=plain'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('')
            lines[1] == render('com.adarshr.test.SingleSpec')
            lines[2] == render('')
            lines[3] == render('  Test this is a single test PASSED')
        and:
            result.task(":test").outcome == SUCCESS
    }

    private static String getSymbol() {
        OperatingSystem.current.windows ? '√' : '✔'
    }
}
