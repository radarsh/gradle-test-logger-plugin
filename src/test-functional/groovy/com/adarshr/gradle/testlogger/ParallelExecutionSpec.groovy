package com.adarshr.gradle.testlogger


import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class ParallelExecutionSpec extends AbstractFunctionalSpec {

    def "parallel execution support with plain-parallel theme"() {
        when:
            def result = run(
                'many-spock-tests',
                '''
                    test {
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger { 
                        theme 'plain-parallel'
                    }
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
                        maxParallelForks = gradle.startParameter.maxWorkerCount
                    }
                    testlogger { 
                        theme 'plain-parallel'
                        showStandardStreams true
                    }
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
}
