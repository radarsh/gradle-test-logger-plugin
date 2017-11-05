package com.adarshr.gradle.testlogger.renderer

import spock.lang.Specification
import spock.lang.Unroll

import static org.fusesource.jansi.Ansi.Erase.*
import static org.fusesource.jansi.Ansi.ansi

class AnsiTextRendererSpec extends Specification {

    def renderer = new AnsiTextRenderer()

    @Unroll
    def "render #input"() {
        expect:
            renderer.render(input) == expectedOutput.toString()
        where:
            input                           | expectedOutput
            '[red]text'                     | ansi().fgRed().a('text')
            '[bright-red]text'              | ansi().fgBrightRed().a('text')
            '[foo]text'                     | '[foo]text'
            '[red,foo]text'                 | ansi().fgRed().a('[foo]text')
            '[yellow]text'                  | ansi().fgYellow().a('text')
            '[bright-yellow]text'           | ansi().fgBrightYellow().a('text')
            '[green]text'                   | ansi().fgGreen().a('text')
            '[bright-green]text'            | ansi().fgBrightGreen().a('text')
            '[bold][red]text[/]'            | ansi().bold().fgRed().a('text').reset()
            '[bold,red]text[/]'             | ansi().bold().fgRed().a('text').reset()
            '[erase-all,bold,red]text[/]'   | ansi().eraseLine(ALL).bold().fgRed().a('text').reset()
            '[erase-ahead,bold,red]text[/]' | ansi().eraseLine(FORWARD).bold().fgRed().a('text').reset()
            '[erase-back,bold,red]text[/]'  | ansi().eraseLine(BACKWARD).bold().fgRed().a('text').reset()
            '[cursor-up-line]text[/]'       | ansi().cursorUpLine().a('text').reset()
            '[red]\\[red\\][/]'             | ansi().fgRed().a('[red]').reset()
            '[bold]text[bold-off]'          | ansi().bold().a('text').boldOff()
            '[default]text'                 | ansi().fgDefault().a('text')
            '[grey]text'                    | ansi().fgBrightBlack().a('text')
            '[cyan]text'                    | ansi().fgCyan().a('text')
            '[bright-cyan]text'             | ansi().fgBrightCyan().a('text')
            '[blue]text'                    | ansi().fgBlue().a('text')
            '[bright-blue]text'             | ansi().fgBrightBlue().a('text')
            '[magenta]text'                 | ansi().fgMagenta().a('text')
            '[bright-magenta]text'          | ansi().fgBrightMagenta().a('text')
    }
}
