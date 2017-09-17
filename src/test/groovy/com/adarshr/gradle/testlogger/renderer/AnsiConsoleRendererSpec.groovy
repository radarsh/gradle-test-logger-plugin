package com.adarshr.gradle.testlogger.renderer

import spock.lang.Specification
import spock.lang.Unroll

import static org.fusesource.jansi.Ansi.Erase.ALL
import static org.fusesource.jansi.Ansi.ansi

class AnsiConsoleRendererSpec extends Specification {

    def renderer = new AnsiConsoleRenderer()

    @Unroll
    def "render #input"() {
        expect:
            renderer.render(input) == expectedOutput.toString()
        where:
            input                          | expectedOutput
            '[red]text'                    | ansi().fgRed().a('text')
            '[foo]text'                    | '[foo]text'
            '[red,foo]text'                | ansi().fgRed().a('[foo]text')
            '[yellow]text'                 | ansi().fgYellow().a('text')
            '[green]text'                  | ansi().fgGreen().a('text')
            '[bold][red]text[/]'           | ansi().bold().fgRed().a('text').reset()
            '[bold,red]text[/]'            | ansi().bold().fgRed().a('text').reset()
            '[erase-line,bold,red]text[/]' | ansi().eraseLine(ALL).bold().fgRed().a('text').reset()
            '[red]\\[red\\][/]'            | ansi().fgRed().a('[red]').reset()
    }
}
