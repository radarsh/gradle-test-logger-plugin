package com.adarshr.gradle.testlogger

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class TestLoggerPluginSpec extends AbstractFunctionalSpec {

    def "log spock tests"() {
        when:
            def result = run('sample-spock-tests', 'clean test')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 27
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.FirstSpec[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            lines[3] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED')
            lines[4..11].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.FirstSpec.this test should fail(FirstSpec.groovy:17)
                   |[/]'''.stripMargin())
            lines[12] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
            lines[13] == render('')
            lines[14] == render('[bold,bright-yellow]com.adarshr.test.SecondSpec[/]')
            lines[15] == render('')
            lines[16] == render('[bold]  Test [/]this test should pass[erase-ahead,green] PASSED[/]')
            lines[17] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED')
            lines[18..25].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.SecondSpec.this test should fail(SecondSpec.groovy:17)
                   |[/]'''.stripMargin())
            lines[26] == render('[bold]  Test [/]this test should be skipped[erase-ahead,yellow] SKIPPED[/]')
        and:
            result.task(":test").outcome == FAILED
    }

    def "run failing test with showExceptions false"() {
        when:
            def result = run('sample-spock-tests', 'testlogger { showExceptions false }', 'clean test --tests *FirstSpec*fail')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 4
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.FirstSpec[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]this test should fail[erase-ahead,red] FAILED[/]')
            lines[3] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "log junit4 tests"() {
        when:
            def result = run('sample-junit4-tests', 'clean test')
        then:
            def lines = getLoggerOutput(result.output)
        and:
            lines.size() == 19
            lines[0] == render('[bold,bright-yellow]com.adarshr.test.FirstTest[/]')
            lines[1] == render('')
            lines[2] == render('[bold]  Test [/]thisTestShouldBeSkipped[erase-ahead,yellow] SKIPPED[/]')
            lines[3] == render('[bold]  Test [/]thisTestShouldFail[erase-ahead,red] FAILED')
            lines[4..7].join('\n') == render(
                '''|
                   |  java.lang.AssertionError: expected:<1> but was:<2>
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:16)
                   |[/]'''.stripMargin())
            lines[8] == render('[bold]  Test [/]thisTestShouldPass[erase-ahead,green] PASSED[/]')
            lines[9] == render('')
            lines[10] == render('[bold,bright-yellow]com.adarshr.test.SecondTest[/]')
            lines[11] == render('')
            lines[12] == render('[bold]  Test [/]thisTestShouldBeSkipped[erase-ahead,yellow] SKIPPED[/]')
            lines[13] == render('[bold]  Test [/]thisTestShouldFail[erase-ahead,red] FAILED')
            lines[14..17].join('\n') == render(
                '''|
                   |  java.lang.AssertionError: expected:<1> but was:<2>
                   |      at com.adarshr.test.SecondTest.thisTestShouldFail(SecondTest.java:16)
                   |[/]'''.stripMargin())
            lines[18] == render('[bold]  Test [/]thisTestShouldPass[erase-ahead,green] PASSED[/]')
        and:
            result.task(":test").outcome == FAILED
    }

    def "do not print empty suites when filtering tests"() {
        when:
            def result = run('sample-spock-tests', 'clean test --tests *SecondSpec*pass')
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
            def result = run('single-spock-test', '''
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
