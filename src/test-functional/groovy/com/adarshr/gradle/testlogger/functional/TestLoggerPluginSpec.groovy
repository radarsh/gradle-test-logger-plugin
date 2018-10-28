package com.adarshr.gradle.testlogger.functional

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class TestLoggerPluginSpec extends AbstractFunctionalSpec {

    def "log spock tests with all the default options"() {
        when:
            def result = run(
                'sample-spock-tests',
                'clean test --tests *FirstSpec --tests *SecondSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
        and:
            lines.size() == 28
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
            lines[4] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[5..12].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.FirstSpec.this test should fail(FirstSpec.groovy:41)
                   |[/]'''.stripMargin())
            lines[13] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
            lines[14] == render('')
            lines[15] == render('[erase-ahead,bold]com.adarshr.test.SecondSpec[/]')
            lines[16] == render('')
            lines[17] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
            lines[18] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[19..26].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.SecondSpec.this test should fail(SecondSpec.groovy:41)
                   |[/]'''.stripMargin())
            lines[27] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
        and:
            result.task(":test").outcome == FAILED
        and:
            def summary = output.summary
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 6 tests in')
            summary[1].endsWith render('(2 failed, 2 skipped)[/]')
            summary[2] == render('')
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
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[/]')
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
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldBeSkipped[yellow] SKIPPED[/]')
            lines[4] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldFail[red] FAILED[red]')
            lines[5..8].join('\n') == render(
                '''|
                   |  java.lang.AssertionError: expected:<1> but was:<2>
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:21)
                   |[/]'''.stripMargin())
            lines[9] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldPass[green] PASSED[/]')
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
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldBeSkipped[yellow] SKIPPED[/]')
            lines[4] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldFail[red] FAILED[red]')
            lines[5..8].join('\n') == render(
                '''|
                   |  java.lang.AssertionError: expected:<1> but was:<2>
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:21)
                   |[/]'''.stripMargin())
            lines[9] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldPass[green] PASSED[/]')
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
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldBeSkipped()[yellow] SKIPPED[/]')
            lines[4] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[5..8].join('\n') == render(
                '''|
                   |  org.opentest4j.AssertionFailedError: expected: <1> but was: <2>
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:18)
                   |[/]'''.stripMargin())
            lines[9] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldPass()[green] PASSED[/]')
        and:
            result.task(":test").outcome == FAILED
    }

    def "log junit5 jupiter engine nested tests"() {
        when:
            def result = run(
                'sample-junit5-jupiter-nested-tests',
                'clean test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 14
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.NestedTest$NestedTestsetOne[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]secondTestOfNestedTestsetOne()[green] PASSED[/]')
            lines[4] == render('[erase-ahead,bold]  Test [bold-off]firstTestOfNestedTestsetOne()[green] PASSED[/]')
            lines[5] == render('')
            lines[6] == render('[erase-ahead,bold]com.adarshr.test.NestedTest$NestedTestsetThree[/]')
            lines[7] == render('')
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]firstTestOfNestedTestsetThree()[green] PASSED[/]')
            lines[9] == render('')
            lines[10] == render('[erase-ahead,bold]com.adarshr.test.NestedTest$NestedTestsetTwo[/]')
            lines[11] == render('')
            lines[12] == render('[erase-ahead,bold]  Test [bold-off]secondTestOfNestedTestsetTwo()[green] PASSED[/]')
            lines[13] == render('[erase-ahead,bold]  Test [bold-off]firstTestOfNestedTestsetTwo()[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log junit5 jupiter engine deep-nested tests"() {
        when:
            def result = run(
                'sample-junit5-jupiter-deep-nested-tests',
                'clean test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 8
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.DeepNestedTest$NestedTestsetLevelOne[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]nestedTestsetLevelOne()[green] PASSED[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.DeepNestedTest$NestedTestsetLevelOne$NestedTestsetLevelTwo[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]nestedTestsetLevelTwo()[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log testng tests"() {
        when:
            def result = run(
                'sample-testng-tests',
                'clean test --tests *First*'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            // No skipped tests due to https://github.com/gradle/gradle/issues/1403
            lines.size() == 9
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldFail[red] FAILED[red]')
            lines[4..7].join('\n') == render(
                '''|
                   |  java.lang.AssertionError: expected [2] but found [1]
                   |      at com.adarshr.test.FirstTest.thisTestShouldFail(FirstTest.java:16)
                   |[/]'''.stripMargin())
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]thisTestShouldPass[green] PASSED[/]')
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
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.SecondSpec[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
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
            lines[0] == render('')
            lines[1] == render('com.adarshr.test.SingleSpec')
            lines[2] == render('')
            lines[3] == render('  Test this is a single test PASSED')
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
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.SlowSpec[/]')
            lines[2] == render('')
            lines[3].startsWith render('[erase-ahead,bold]  Test [bold-off]this is a slow test[green] PASSED[red]')
            lines[3] ==~ /.*\(3\.?\d?s\)\u001B\[m$/
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
            output.summary == ['']
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
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.SingleSpec[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]this is a single test[green] PASSED[/]')
            lines[8] == render('[default]')
            lines[9] == render('    stdout setup')
            lines[10] == render('    stderr setup')
            lines[11] == render('    stdout expect')
            lines[12] == render('    stderr expect')
            lines[13] == render('    stdout cleanup')
            lines[14] == render('    stderr cleanup[/]')
            lines[15] == render('')
            lines[16] == render('[default]')
            lines[17] == render('  stdout cleanupSpec')
            lines[18] == render('  stderr cleanupSpec[/]')
            lines[19] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "hide passed tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger { 
                        showPassed false
                        showExceptions false
                    }
                ''',
                'clean test --tests *FirstSpec --tests *SecondSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 10
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[/]')
            lines[4] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
            lines[5] == render('')
            lines[6] == render('[erase-ahead,bold]com.adarshr.test.SecondSpec[/]')
            lines[7] == render('')
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[/]')
            lines[9] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 6 tests in')
            summary[1].endsWith render('(2 failed, 2 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hide passed and skipped tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger { 
                        showPassed false
                        showSkipped false
                        showExceptions false
                    }
                ''',
                'clean test --tests *FirstSpec --tests *SecondSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 8
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.SecondSpec[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[/]')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 6 tests in')
            summary[1].endsWith render('(2 failed, 2 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hide passed, skipped and failed tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger { 
                        showPassed false
                        showSkipped false
                        showFailed false
                        showExceptions false
                    }
                ''',
                'clean test --tests *FirstSpec --tests *SecondSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 0
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 6 tests in')
            summary[1].endsWith render('(2 failed, 2 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hiding tests also hides its corresponding standard stream output"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger { 
                        showPassed false
                        showSkipped false
                        showExceptions false
                        showStandardStreams true
                    }
                ''',
                'clean test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 20
            lines[0] == render('[default]')
            lines[1] == render('  FirstSpec - stdout setupSpec')
            lines[2] == render('  FirstSpec - stderr setupSpec[/]')
            lines[3] == render('')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[/]')
            lines[8] == render('[default]')
            lines[9] == render('    FirstSpec - this test should fail - stdout setup')
            lines[10] == render('    FirstSpec - this test should fail - stderr setup')
            lines[11] == render('    FirstSpec - this test should fail - stdout expect')
            lines[12] == render('    FirstSpec - this test should fail - stderr expect')
            lines[13] == render('    FirstSpec - this test should fail - stdout cleanup')
            lines[14] == render('    FirstSpec - this test should fail - stderr cleanup[/]')
            lines[15] == render('')
            lines[16] == render('[default]')
            lines[17] == render('  FirstSpec - stdout cleanupSpec')
            lines[18] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[19] == render('')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 3 tests in')
            summary[1].endsWith render('(1 failed, 1 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hiding passed tests hides all output from suite if all tests in the suite pass"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger {
                        showPassed false
                        showSkipped true
                        showFailed false
                        showStandardStreams true
                    }
                ''',
                'clean test --tests *FirstSpec --tests *ThirdSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 12
            lines[0] == render('[default]')
            lines[1] == render('  FirstSpec - stdout setupSpec')
            lines[2] == render('  FirstSpec - stderr setupSpec[/]')
            lines[3] == render('')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
            lines[8] == render('[default]')
            lines[9] == render('  FirstSpec - stdout cleanupSpec')
            lines[10] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[11] == render('')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 6 tests in')
            summary[1].endsWith render('(1 failed, 1 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hide standard stream output for passed tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger {
                        showStandardStreams true
                        showPassedStandardStreams false
                    }
                ''',
                'clean test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 30
            lines[0] == render('[default]')
            lines[1] == render('  FirstSpec - stdout setupSpec')
            lines[2] == render('  FirstSpec - stderr setupSpec[/]')
            lines[3] == render('')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[9..16].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.FirstSpec.this test should fail(FirstSpec.groovy:41)
                   |[/]'''.stripMargin())
            lines[17] == render('[default]')
            lines[18] == render('    FirstSpec - this test should fail - stdout setup')
            lines[19] == render('    FirstSpec - this test should fail - stderr setup')
            lines[20] == render('    FirstSpec - this test should fail - stdout expect')
            lines[21] == render('    FirstSpec - this test should fail - stderr expect')
            lines[22] == render('    FirstSpec - this test should fail - stdout cleanup')
            lines[23] == render('    FirstSpec - this test should fail - stderr cleanup[/]')
            lines[24] == render('')
            lines[25] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
            lines[26] == render('[default]')
            lines[27] == render('  FirstSpec - stdout cleanupSpec')
            lines[28] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[29] == render('')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 3 tests in')
            summary[1].endsWith render('(1 failed, 1 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hide standard stream output for passed and failed tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger {
                        showStandardStreams true
                        showPassedStandardStreams false
                        showFailedStandardStreams false
                    }
                ''',
                'clean test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 22
            lines[0] == render('[default]')
            lines[1] == render('  FirstSpec - stdout setupSpec')
            lines[2] == render('  FirstSpec - stderr setupSpec[/]')
            lines[3] == render('')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[green] PASSED[/]')
            lines[8] == render('[erase-ahead,bold]  Test [bold-off]this test should fail[red] FAILED[red]')
            lines[9..16].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.FirstSpec.this test should fail(FirstSpec.groovy:41)
                   |[/]'''.stripMargin())
            lines[17] == render('[erase-ahead,bold]  Test [bold-off]this test should be skipped[yellow] SKIPPED[/]')
            lines[18] == render('[default]')
            lines[19] == render('  FirstSpec - stdout cleanupSpec')
            lines[20] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[21] == render('')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 3 tests in')
            summary[1].endsWith render('(1 failed, 1 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "each test task can have its own testlogger extension"() {
        given:
            def buildFragment = '''
                testlogger { 
                    theme 'standard' 
                }
                test {
                    testlogger {
                        theme 'plain'
                    }
                }
                task anotherTask(type: Test) {
                    testlogger {
                        theme 'plain-parallel'
                    }
                }
            '''
        when:
            def result = run(
                'single-spock-test',
                buildFragment,
                'clean test'
            )
            def lines = getLoggerOutput(result.output).lines
        then:
            lines.size() == 4
            lines[0] == render('')
            lines[1] == render('com.adarshr.test.SingleSpec')
            lines[2] == render('')
            lines[3] == render('  Test this is a single test PASSED')
        and:
            result.task(':test').outcome == SUCCESS
        when:
            result = run(
                'single-spock-test',
                buildFragment,
                'clean anotherTask'
            )
            lines = getLoggerOutput(result.output).lines
        then:
            lines.size() == 1
            lines[0] == render('com.adarshr.test.SingleSpec this is a single test PASSED')
        and:
            result.task(':anotherTask').outcome == SUCCESS
    }
}
