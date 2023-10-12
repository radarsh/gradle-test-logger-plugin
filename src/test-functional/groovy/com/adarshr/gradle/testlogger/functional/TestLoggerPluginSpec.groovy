package com.adarshr.gradle.testlogger.functional


import java.nio.file.Files

import static org.gradle.testkit.runner.TaskOutcome.FAILED
import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class TestLoggerPluginSpec extends AbstractFunctionalSpec {

    def "log spock tests with all the default options"() {
        when:
            def result = run(
                'sample-spock-tests',
                'testlogger { slowThreshold 5000 }',
                'test --tests *FirstSpec --tests *SecondSpec'
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
                '''
                    testlogger {
                        slowThreshold 5000
                        showExceptions false 
                    }
                ''',
                'test --tests *FirstSpec*fail'
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
                'testlogger { slowThreshold 5000 }',
                'test --tests *First*'
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
                'testlogger { slowThreshold 5000 }',
                'test --tests *First*'
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
                'testlogger { slowThreshold 5000 }',
                'test --tests *First*'
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

    def "log junit5 jupiter engine parameterised tests"() {
        when:
            def result = run(
                'sample-junit5-jupiter-tests',
                'testlogger { slowThreshold 5000 }',
                'test --tests *ParamTest'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 8
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.ParamTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  Testing parameterized tests[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]    Test [bold-off]param One is not null[green] PASSED[/]')
            lines[6] == render('[erase-ahead,bold]    Test [bold-off]param Two is not null[green] PASSED[/]')
            lines[7] == render('[erase-ahead,bold]    Test [bold-off]param Three is not null[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log junit5 jupiter engine nested tests"() {
        when:
            def result = run(
                'sample-junit5-jupiter-nested-tests',
                'testlogger { slowThreshold 5000 }',
                'test'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 16
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.NestedTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  NestedTestsetThree[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]    Test [bold-off]firstTestOfNestedTestsetThree()[green] PASSED[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  NestedTestsetTwo[/]')
            lines[8] == render('')
            lines[9] == render('[erase-ahead,bold]    Test [bold-off]secondTestOfNestedTestsetTwo()[green] PASSED[/]')
            lines[10] == render('[erase-ahead,bold]    Test [bold-off]firstTestOfNestedTestsetTwo()[green] PASSED[/]')
            lines[11] == render('')
            lines[12] == render('[erase-ahead,bold]  NestedTestsetOne[/]')
            lines[13] == render('')
            lines[14] == render('[erase-ahead,bold]    Test [bold-off]secondTestOfNestedTestsetOne()[green] PASSED[/]')
            lines[15] == render('[erase-ahead,bold]    Test [bold-off]firstTestOfNestedTestsetOne()[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log junit5 jupiter engine deep-nested tests"() {
        when:
            def result = run(
                'sample-junit5-jupiter-deep-nested-tests',
                'testlogger { slowThreshold 5000 }',
                'clean test'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 10
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.DeepNestedTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  NestedTestsetLevelOne[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]    Test [bold-off]nestedTestsetLevelOne()[green] PASSED[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]    Nested test set level two[/]')
            lines[8] == render('')
            lines[9] == render('[erase-ahead,bold]      Test [bold-off]nestedTestsetLevelTwo()[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log junit5 jupiter engine deep-nested tests when showStandardStreams is true and theme is standard"() {
        when:
            def result = run(
                'sample-junit5-jupiter-deep-nested-tests',
                '''
                    testlogger { 
                        slowThreshold 5000
                        showStandardStreams true
                        theme 'standard' 
                    }
                ''',
                'test'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 22
            lines[0] == render('')
            lines[1] == render('[default]')
            lines[2] == render('  DeepNestedTest.beforeAllDeepNestedTest[/]')
            lines[3] == render('')
            lines[4] == render('[erase-ahead,bold]com.adarshr.test.DeepNestedTest[/]')
            lines[5] == render('')
            lines[6] == render('[erase-ahead,bold]  NestedTestsetLevelOne[/]')
            lines[7] == render('')
            lines[8] == render('[erase-ahead,bold]    Test [bold-off]nestedTestsetLevelOne()[green] PASSED[/]')
            lines[9] == render('[default]')
            lines[10] == render('      NestedTestsetLevelOne.nestedTestsetLevelOne[/]')
            lines[11] == render('')
            lines[12] == render('')
            lines[13] == render('[erase-ahead,bold]    Nested test set level two[/]')
            lines[14] == render('')
            lines[15] == render('[erase-ahead,bold]      Test [bold-off]nestedTestsetLevelTwo()[green] PASSED[/]')
            lines[16] == render('[default]')
            lines[17] == render('        NestedTestsetLevelTwo.nestedTestsetLevelTwo[/]')
            lines[18] == render('')
            lines[19] == render('[default]')
            lines[20] == render('  DeepNestedTest.afterAllDeepNestedTest[/]')
            lines[21] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log junit5 jupiter engine deep-nested tests when showStandardStreams is true and theme is mocha"() {
        when:
            def result = run(
                'sample-junit5-jupiter-deep-nested-tests',
                '''
                    testlogger { 
                        slowThreshold 5000
                        showStandardStreams true
                        theme 'mocha' 
                    }
                ''',
                'test'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 22
            lines[0] == render('')
            lines[1] == render('[grey]')
            lines[2] == render('    DeepNestedTest.beforeAllDeepNestedTest[/]')
            lines[3] == render('')
            lines[4] == render('  [erase-ahead,default]com.adarshr.test.DeepNestedTest[/]')
            lines[5] == render('')
            lines[6] == render('    [erase-ahead,default]NestedTestsetLevelOne[/]')
            lines[7] == render('')
            lines[8] == render("      [erase-ahead,green]${passedSymbol}[grey] nestedTestsetLevelOne()[/]")
            lines[9] == render('[grey]')
            lines[10] == render('          NestedTestsetLevelOne.nestedTestsetLevelOne[/]')
            lines[11] == render('')
            lines[12] == render('')
            lines[13] == render('      [erase-ahead,default]Nested test set level two[/]')
            lines[14] == render('')
            lines[15] == render("        [erase-ahead,green]${passedSymbol}[grey] nestedTestsetLevelTwo()[/]")
            lines[16] == render('[grey]')
            lines[17] == render('            NestedTestsetLevelTwo.nestedTestsetLevelTwo[/]')
            lines[18] == render('')
            lines[19] == render('[grey]')
            lines[20] == render('    DeepNestedTest.afterAllDeepNestedTest[/]')
            lines[21] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log junit5 jupiter engine deep-nested tests when showStandardStreams is true and theme is standard-parallel"() {
        when:
            def result = run(
                'sample-junit5-jupiter-deep-nested-tests',
                '''
                    testlogger { 
                        slowThreshold 5000
                        showStandardStreams true
                        theme 'standard-parallel' 
                    }
                ''',
                'test'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 15
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.DeepNestedTest > NestedTestsetLevelOne[bold-off] nestedTestsetLevelOne()[green] PASSED[/]')
            lines[2] == render('[default]')
            lines[3] == render('  DeepNestedTest.beforeAllDeepNestedTest[/]')
            lines[4] == render('')
            lines[5] == render('[default]')
            lines[6] == render('  NestedTestsetLevelOne.nestedTestsetLevelOne[/]')
            lines[7] == render('')
            lines[8] == render('[erase-ahead,bold]com.adarshr.test.DeepNestedTest > NestedTestsetLevelOne > Nested test set level two[bold-off] nestedTestsetLevelTwo()[green] PASSED[/]')
            lines[9] == render('[default]')
            lines[10] == render('  NestedTestsetLevelTwo.nestedTestsetLevelTwo[/]')
            lines[11] == render('')
            lines[12] == render('[default]')
            lines[13] == render('  DeepNestedTest.afterAllDeepNestedTest[/]')
            lines[14] == render('')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log junit5 jupiter engine deep-nested tests when showSimpleNames is true"() {
        when:
            def result = run(
                'sample-junit5-jupiter-deep-nested-tests',
                '''
                    testlogger { 
                        showSimpleNames true
                        slowThreshold 5000
                    }
                ''',
                'test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 10
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]DeepNestedTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  NestedTestsetLevelOne[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]    Test [bold-off]nestedTestsetLevelOne()[green] PASSED[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]    Nested test set level two[/]')
            lines[8] == render('')
            lines[9] == render('[erase-ahead,bold]      Test [bold-off]nestedTestsetLevelTwo()[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log kotest tests"() {
        when:
            def result = run(
                'sample-kotest-tests',
                'test'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 15
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.WordSpecTest[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  a context should[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]    Test [bold-off]have a test[green] PASSED[/]')
            lines[6] == render('[erase-ahead,bold]    Test [bold-off]have another test[green] PASSED[/]')
            lines[7] == render('[erase-ahead,bold]    Test [bold-off]have a test with config[yellow] SKIPPED[/]')
            lines[8] == render('')
            lines[9] == render('[erase-ahead,bold]  another context when[/]')
            lines[10] == render('')
            lines[11] == render('[erase-ahead,bold]    using when should[/]')
            lines[12] == render('')
            lines[13] == render('[erase-ahead,bold]      Test [bold-off]have a test[green] PASSED[/]')
            lines[14] == render('[erase-ahead,bold]      Test [bold-off]have a test with config[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log spek tests"() {
        when:
            def result = run(
                'sample-spek-tests',
                'test'
            )
        then:
            def lines = getNestedLoggerOutput(result.output).lines
        and:
            lines.size() == 8
            lines[0] == render('')
            lines[1] == render('[erase-ahead,bold]com.adarshr.test.CalculatorSpec[/]')
            lines[2] == render('')
            lines[3] == render('[erase-ahead,bold]  A calculator[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]    addition[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]      Test [bold-off]returns the sum of its arguments[green] PASSED[/]')
        and:
            result.task(":test").outcome == SUCCESS
    }

    def "log testng tests"() {
        when:
            def result = run(
                'sample-testng-tests',
                'testlogger { slowThreshold 5000 }',
                'test --tests *First*'
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

    def "log spock tests when showSimpleNames is true"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger { 
                        theme 'plain' 
                        showSimpleNames true
                        slowThreshold 5000
                    }
                ''',
                'test'
            )
            def lines = getLoggerOutput(result.output).lines
        then:
            lines.size() == 4
            lines[0] == render('')
            lines[1] == render('SingleSpec')
            lines[2] == render('')
            lines[3] == render('  Test this is a single test PASSED')
        and:
            result.task(':test').outcome == SUCCESS
    }

    def "do not print empty suites when filtering tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                'testlogger { slowThreshold 5000 }',
                'test --tests *SecondSpec*pass'
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
                        slowThreshold 5000
                    }
                    task anotherTask(type: Test) {
                        useJUnitPlatform()
                    }
                ''',
                'anotherTask'
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
                'test'
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
                        slowThreshold 5000
                    }
                ''',
                'test'
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
                        slowThreshold 5000
                    }
                ''',
                'test'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 20
            lines[0] == render('')
            lines[1] == render('[default]')
            lines[2] == render('  stdout setupSpec')
            lines[3] == render('  stderr setupSpec[/]')
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

    def "show standard streams from before System exit was called from setupSpec"() {
        when:
            def result = run(
                'sample-spock-tests-system-exit',
                '''
                    testlogger { 
                        showStandardStreams true
                        slowThreshold 5000
                    }
                ''',
                'test --tests *SecondSpec'
            )
        then:
            def lines = getLoggerOutput(result.output).lines
        and:
            lines.size() == 4
            lines[0] == render('[default]')
            lines[1] == render('  SecondSpec - stdout setupSpec')
            lines[2] == render('  SecondSpec - stderr setupSpec[/]')
            lines[3] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "show standard streams from before System exit was called from setup"() {
        when:
            def result = run(
                'sample-spock-tests-system-exit',
                '''
                    testlogger { 
                        showStandardStreams true
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 12
            lines[0] == render('')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
            lines[4] == render('')
            lines[5] == render('[erase-ahead,bold]com.adarshr.test.FirstSpec[/]')
            lines[6] == render('')
            lines[7] == render('[erase-ahead,bold]  Test [bold-off]this test should pass[yellow] SKIPPED[/]')
            lines[8] == render('[default]')
            lines[9] == render('    FirstSpec - this test should pass - stdout setup')
            lines[10] == render('    FirstSpec - this test should pass - stderr setup[/]')
        and:
            summary[0] == render('')
            summary[1].startsWith render('[erase-ahead,bold,green]SUCCESS: [default]Executed 1 tests in')
            summary[1].endsWith render('(1 skipped)[/]')
            summary[2] == render('')
        and:
            result.task(":test").outcome == FAILED
    }

    def "hide passed tests"() {
        when:
            def result = run(
                'sample-spock-tests',
                '''
                    testlogger { 
                        showPassed false
                        showExceptions false
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec --tests *SecondSpec'
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
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec --tests *SecondSpec'
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
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec --tests *SecondSpec'
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
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 20
            lines[0] == render('')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
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
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec --tests *ThirdSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 12
            lines[0] == render('')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
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
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 30
            lines[0] == render('')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
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
                        slowThreshold 5000
                    }
                ''',
                'test --tests *FirstSpec'
            )
        then:
            def output = getLoggerOutput(result.output)
            def lines = output.lines
            def summary = output.summary
        and:
            lines.size() == 22
            lines[0] == render('')
            lines[1] == render('[default]')
            lines[2] == render('  FirstSpec - stdout setupSpec')
            lines[3] == render('  FirstSpec - stderr setupSpec[/]')
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

    def "run test with alternate log level higher than gradle log level"() {
        when:
            def result = run(
                'single-spock-test',
                '''
                    testlogger {
                        theme 'plain'
                        logLevel 'quiet'
                        slowThreshold 5000
                    }
                    markerLevel = 'quiet'
                ''',
                'test --quiet'
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
    }

    def "each test task can have its own testlogger extension"() {
        given:
            def buildFragment = '''
                testlogger { 
                    theme 'standard' 
                }
                test {
                    useJUnitPlatform()
                    testlogger {
                        theme 'plain'
                        slowThreshold 5000
                    }
                }
                task anotherTask(type: Test) {
                    useJUnitPlatform()
                    testlogger {
                        theme 'plain-parallel'
                        slowThreshold 5000
                    }
                }
            '''
        when:
            def result = run(
                'single-spock-test',
                buildFragment,
                'test'
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
            temporaryFolder = Files.createTempDirectory('spock')
            result = run(
                'single-spock-test',
                buildFragment,
                'anotherTask'
            )
            lines = getLoggerOutput(result.output).lines
        then:
            lines.size() == 2
            lines[0] == render('')
            lines[1] == render('com.adarshr.test.SingleSpec this is a single test PASSED')
        and:
            result.task(':anotherTask').outcome == SUCCESS
    }
}
