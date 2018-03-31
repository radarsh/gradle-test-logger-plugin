package com.adarshr.gradle.testlogger

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class TestLoggerPluginSpec extends AbstractFunctionalSpec {

    def "log spock tests with all the default options"() {
        when:
            def result = run(
                'sample-spock-tests',
                'clean test'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
        and:
            lines.size() == 28
            lines[0] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.FirstSpec[/]')
            lines[1] == render('')
            lines[2] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[4..11].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.FirstSpec.this test should fail(FirstSpec.groovy:17)
                   |[/]'''.stripMargin())
            lines[12] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
            lines[13] == render('')
            lines[14] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.SecondSpec[/]')
            lines[15] == render('')
            lines[16] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
            lines[17] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[18..25].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.SecondSpec.this test should fail(SecondSpec.groovy:17)
                   |[/]'''.stripMargin())
            lines[26] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
            lines[27] == render('')
        and:
            result.task(":test").outcome == FAILED
        and:
            def summary = output.summary
            summary[0].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 6 tests in')
            summary[0].endsWith render('(2 failed, 2 skipped)[/]')
            summary[1] == render('')
    }

    def "run failing test with showExceptions false"() {
        when:
            def result = run(
                'sample-spock-tests',
                'testlogger { showExceptions false }',
                'clean test --tests *FirstSpec*fail'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.FirstSpec[/]')
            lines[1] == render('')
            lines[2] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[/]')
            lines[3] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "log junit4 tests"() {
        when:
            def result = run(
                'sample-junit4-tests',
                'clean test --tests *First*'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 10
            lines[0] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.FirstTest[/]')
            lines[1] == render('')
            lines[2] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldBeSkipped[yellow] SKIPPED[/]')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldFail[red] FAILED[red]')
            lines[4..7].join('\n') == render(
                '''|
                   |  java.lang.AssertionError: expected:<1> but was:<2>
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:21)
                   |[/]'''.stripMargin())
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldPass[green] PASSED[/]')
            lines[9] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "log junit5 vintage engine tests"() {
        when:
            def result = run(
                'sample-junit5-vintage-tests',
                'clean test --tests *First*'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 10
            lines[0] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.FirstTest[/]')
            lines[1] == render('')
            lines[2] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldBeSkipped[yellow] SKIPPED[/]')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldFail[red] FAILED[red]')
            lines[4..7].join('\n') == render(
                '''|
                   |  java.lang.AssertionError: expected:<1> but was:<2>
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:21)
                   |[/]'''.stripMargin())
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldPass[green] PASSED[/]')
            lines[9] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "log junit5 jupiter engine tests"() {
        when:
            def result = run(
                'sample-junit5-jupiter-tests',
                'clean test --tests *First*'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 10
            lines[0] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.FirstTest[/]')
            lines[1] == render('')
            lines[2] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldBeSkipped()[yellow] SKIPPED[/]')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[4..7].join('\n') == render(
                '''|
                   |  org.opentest4j.AssertionFailedError: expected: <1> but was: <2>
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:18)
                   |[/]'''.stripMargin())
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldPass()[green] PASSED[/]')
            lines[9] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "do not print empty suites when filtering tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                'clean test --tests *SecondSpec*pass'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.SecondSpec[/]')
            lines[1] == render('')
            lines[2] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
            lines[3] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "hook into any task of type test"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        theme 'plain' 
                    }
                    task anotherTask(type: Test) { }
                ''',
                'clean anotherTask'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('com.adarshr.test.SingleSpec')
            lines[1] == render('')
            lines[2] == render('  Test this is a single test PASSED')
            lines[3] == render('')
        and:
            result.task(':anotherTask').outcome == SUCCESS
    }

    def "show test execution time for slow tests"() {
        when:
            def result = run(
                'slow-spock-test',
                'testlogger { slowThreshold 1000 }',
                'clean test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.SlowSpec[/]')
            lines[1] == render('')
            lines[2].startsWith render('[erase-ahead,bold]  Test [bold-off]this is a slow test[green] PASSED[red]')
            lines[2] ==~ /.*\(3\.?\d?s\)\u001B\[m$/
            lines[3] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "hide summary display"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        showSummary false
                    }
                ''',
                'clean test'
            )
        then:
            def output = getLoggerOutput(result.output)
        and:
            output.lines.size() == 4
            !output.summary
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "show standard streams"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        showStandardStreams true
                    }
                ''',
                'clean test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 20
            lines[0] == render('[default]')
            lines[1] == render('  stdout setupSpec')
            lines[2] == render('  stderr setupSpec[/]')
            lines[3] == render('')
            lines[4] == render('[erase-ahead,bold,bright-yellow]com.adarshr.test.SingleSpec[/]')
            lines[5] == render('')
            lines[6] == render('[erase-ahead,bold]  Test [bold-off]this is a single test[green] PASSED[/]')
            lines[7] == render('[default]')
            lines[8] == render('    stdout setup')
            lines[9] == render('    stderr setup')
            lines[10] == render('    stdout expect')
            lines[11] == render('    stderr expect')
            lines[12] == render('    stdout cleanup')
            lines[13] == render('    stderr cleanup[/]')
            lines[14] == render('')
            lines[15] == render('[default]')
            lines[16] == render('  stdout cleanupSpec')
            lines[17] == render('  stderr cleanupSpec[/]')
            lines[18] == render('')
            lines[19] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }
}
