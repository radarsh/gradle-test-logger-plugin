package com.adarshr.gradle.testlogger

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class TestLoggerPluginSpec extends AbstractFunctionalSpec {

    def "log spock tests"() {
        when:
            def result = run('sample-spock-tests')
        then:
            def actual = getLoggerOutput(result.output)
        and:
            actual[0] == render('[bold,bright-yellow]com.adarshr.test.FirstSpec[/]')
            actual[1] == render('')
            actual[2] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            actual[3] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED[/]')
            actual[4] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
            actual[5] == render('')
            actual[6] == render('[bold,bright-yellow]com.adarshr.test.SecondSpec[/]')
            actual[7] == render('')
            actual[8] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            actual[9] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED[/]')
            actual[10] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hook into any task of type test"() {
        when:
            def result = run('single-spock-test', '''
                testlogger { theme 'plain' }
                task anotherTask(type: Test) { }
            ''', 'clean anotherTask')
        then:
            def actualLines = getLoggerOutput(result.output)
            actualLines.size() == 3
            actualLines[0] == render('com.adarshr.test.SingleSpec')
            actualLines[1] == render('')
            actualLines[2] == render('  Test this is a single test PASSED')
        and:
            result.task(':anotherTask').outcome == SUCCESS
    }
}
