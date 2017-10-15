package com.adarshr.gradle.testlogger

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class TestLoggerPluginSpec extends AbstractFunctionalSpec {

    def "log spock tests"() {
        when:
            def result = runMultiple('sample-spock-tests')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 11
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.FirstSpec[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            lines[3] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED[/]')
            lines[4] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
            lines[5] == render('')
            lines[6] == render('[bold,bright-yellow]com.adarshr.test.SecondSpec[/]')
            lines[7] == render('')
            lines[8] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            lines[9] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED[/]')
            lines[10] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
        and:
            result.task(":test").outcome == FAILED
    }

    def "log junit4 tests"() {
        when:
            def result = runMultiple('sample-junit4-tests')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 11
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.FirstSpec[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            lines[3] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED[/]')
            lines[4] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
            lines[5] == render('')
            lines[6] == render('[bold,bright-yellow]com.adarshr.test.SecondSpec[/]')
            lines[7] == render('')
            lines[8] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            lines[9] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED[/]')
            lines[10] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
        and:
            result.task(":test").outcome == FAILED
    }

    def "do not print empty suites when filtering tests"() {
        when:
            def result = runMultiple('sample-spock-tests', 'clean test --tests *SecondSpec*pass')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 3
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.SecondSpec[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "hook into any task of type test"() {
        when:
            def result = runSingle('single-spock-test' ,'''
                testlogger { theme 'plain' }
                task anotherTask(type: Test) { }
            ''', 'clean anotherTask')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 3
            lines[0] == render('com.adarshr.test.SingleSpec')
            lines[1] == render('')
            lines[2] == render('  Test this is a single test PASSED')
        and:
            result.task(':anotherTask').outcome == SUCCESS
    }
}
