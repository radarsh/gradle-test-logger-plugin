package com.adarshr.gradle.testlogger.functional


import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ParallelExecutionSpec extends AbstractFunctionalSpec {

    def "parallel execution support with plain-parallel theme"() {
        when:
            def result = run(
                'many-spock-tests',
                '''
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger { 
                        theme 'plain-parallel'
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                 ''',
                'clean test'
            )
        then:
            def lines = getParallelLoggerOutput(result.output).lines
        and:
            lines.size() == 20
            lines[0] == render('com.adarshr.test.FiveSpec this is test four PASSED')
            lines[1] == render('com.adarshr.test.FiveSpec this is test one PASSED')
            lines[2] == render('com.adarshr.test.FiveSpec this is test three PASSED')
            lines[3] == render('com.adarshr.test.FiveSpec this is test two PASSED')
            lines[4] == render('com.adarshr.test.FourSpec this is test four PASSED')
            lines[5] == render('com.adarshr.test.FourSpec this is test one PASSED')
            lines[6] == render('com.adarshr.test.FourSpec this is test three PASSED')
            lines[7] == render('com.adarshr.test.FourSpec this is test two PASSED')
            lines[8] == render('com.adarshr.test.OneSpec this is test four PASSED')
            lines[9] == render('com.adarshr.test.OneSpec this is test one PASSED')
            lines[10] == render('com.adarshr.test.OneSpec this is test three PASSED')
            lines[11] == render('com.adarshr.test.OneSpec this is test two PASSED')
            lines[12] == render('com.adarshr.test.ThreeSpec this is test four PASSED')
            lines[13] == render('com.adarshr.test.ThreeSpec this is test one PASSED')
            lines[14] == render('com.adarshr.test.ThreeSpec this is test three PASSED')
            lines[15] == render('com.adarshr.test.ThreeSpec this is test two PASSED')
            lines[16] == render('com.adarshr.test.TwoSpec this is test four PASSED')
            lines[17] == render('com.adarshr.test.TwoSpec this is test one PASSED')
            lines[18] == render('com.adarshr.test.TwoSpec this is test three PASSED')
            lines[19] == render('com.adarshr.test.TwoSpec this is test two PASSED')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "parallel execution support with plain-parallel theme and showStandardStreams true"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger { 
                        theme 'plain-parallel'
                        showStandardStreams true
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                 ''',
                'clean test'
            )
        then:
            def lines = getParallelLoggerOutput(result.output).lines
        and:
            lines.size() == 17
            lines[0] == render('com.adarshr.test.SingleSpec this is a single test PASSED')
            lines[1] == render('')
            lines[2] == render('  stdout setupSpec')
            lines[3] == render('  stderr setupSpec')
            lines[4] == render('')
            lines[5] == render('')
            lines[6] == render('  stdout setup')
            lines[7] == render('  stderr setup')
            lines[8] == render('  stdout expect')
            lines[9] == render('  stderr expect')
            lines[10] == render('  stdout cleanup')
            lines[11] == render('  stderr cleanup')
            lines[12] == render('')
            lines[13] == render('')
            lines[14] == render('  stdout cleanupSpec')
            lines[15] == render('  stderr cleanupSpec')
            lines[16] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "hide passed tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger {
                        theme 'standard-parallel'
                        showPassed false
                        showExceptions false
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                ''',
                'clean test --tests *FirstSpec --tests *SecondSpec'
            )
        then:
            def output = getParallelLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 4
            lines[0] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should be skipped[yellow] SKIPPED[/]')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should fail[red] FAILED[/]')
            lines[2] == render('[erase-ahead,bold]com.adarshr.test.SecondSpec[bold-off] this test should be skipped[yellow] SKIPPED[/]')
            lines[3] == render('[erase-ahead,bold]com.adarshr.test.SecondSpec[bold-off] this test should fail[red] FAILED[/]')
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
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger {
                        theme 'standard-parallel'
                        showPassed false
                        showSkipped false
                        showExceptions false
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                ''',
                'clean test --tests *FirstSpec --tests *SecondSpec'
            )
        then:
            def output = getParallelLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 2
            lines[0] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should fail[red] FAILED[/]')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.SecondSpec[bold-off] this test should fail[red] FAILED[/]')
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
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger {
                        theme 'standard-parallel'
                        showPassed false
                        showSkipped false
                        showFailed false
                        showExceptions false
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                ''',
                'clean test --tests *FirstSpec --tests *SecondSpec'
            )
        then:
            def output = getParallelLoggerOutput(result.output)
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
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger {
                        theme 'standard-parallel'
                        showPassed false
                        showSkipped false
                        showExceptions false
                        showStandardStreams true
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                ''',
                'clean test --tests *FirstSpec'
            )
        then:
            def output = getParallelLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 17
            lines[0] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should fail[red] FAILED[/]')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
            lines[4] == render('')
            lines[5] == render('[default]')
            lines[6] == render('  FirstSpec - this test should fail - stdout setup')
            lines[7] == render('  FirstSpec - this test should fail - stderr setup')
            lines[8] == render('  FirstSpec - this test should fail - stdout expect')
            lines[9] == render('  FirstSpec - this test should fail - stderr expect')
            lines[10] == render('  FirstSpec - this test should fail - stdout cleanup')
            lines[11] == render('  FirstSpec - this test should fail - stderr cleanup[/]')
            lines[12] == render('')
            lines[13] == render('[default]')
            lines[14] == render('  FirstSpec - stdout cleanupSpec')
            lines[15] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[16] == render('')
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
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger {
                        theme 'standard-parallel'
                        showPassed false
                        showSkipped true
                        showFailed false
                        showStandardStreams true
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                ''',
                'clean test --tests *FirstSpec --tests *ThirdSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 9
            lines[0] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should be skipped[yellow] SKIPPED[/]')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
            lines[4] == render('')
            lines[5] == render('[default]')
            lines[6] == render('  FirstSpec - stdout cleanupSpec')
            lines[7] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[8] == render('')
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
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger {
                        theme 'standard-parallel'
                        showStandardStreams true
                        showPassedStandardStreams false
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                ''',
                'clean test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 27
            lines[0] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should pass[green] PASSED[/]')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should fail[red] FAILED[red]')
            lines[6..13].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.FirstSpec.this test should fail(FirstSpec.groovy:41)
                   |[/]'''.stripMargin())
            lines[14] == render('[default]')
            lines[15] == render('  FirstSpec - this test should fail - stdout setup')
            lines[16] == render('  FirstSpec - this test should fail - stderr setup')
            lines[17] == render('  FirstSpec - this test should fail - stdout expect')
            lines[18] == render('  FirstSpec - this test should fail - stderr expect')
            lines[19] == render('  FirstSpec - this test should fail - stdout cleanup')
            lines[20] == render('  FirstSpec - this test should fail - stderr cleanup[/]')
            lines[21] == render('')
            lines[22] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should be skipped[yellow] SKIPPED[/]')
            lines[23] == render('[default]')
            lines[24] == render('  FirstSpec - stdout cleanupSpec')
            lines[25] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[26] == render('')
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
                    test {
                        useJUnitPlatform()
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger {
                        theme 'standard-parallel'
                        showStandardStreams true
                        showPassedStandardStreams false
                        showFailedStandardStreams false
                        slowThreshold 5000
                    }
                    printFirstNewLine = false
                ''',
                'clean test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 19
            lines[0] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should pass[green] PASSED[/]')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should fail[red] FAILED[red]')
            lines[6..13].join('\n') == render(
                '''|
                   |  Condition not satisfied:
                   |  
                   |  1 == 2
                   |    |
                   |    false
                   |      at com.adarshr.test.FirstSpec.this test should fail(FirstSpec.groovy:41)
                   |[/]'''.stripMargin())
            lines[14] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[bold-off] this test should be skipped[yellow] SKIPPED[/]')
            lines[15] == render('[default]')
            lines[16] == render('  FirstSpec - stdout cleanupSpec')
            lines[17] == render('  FirstSpec - stderr cleanupSpec[/]')
            lines[18] == render('')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,red]FAILURE: [default]Executed 3 tests in')
            summary[1].endsWith render('(1 failed, 1 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }
}
