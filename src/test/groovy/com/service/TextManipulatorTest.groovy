package com.service

import spock.lang.Specification

class TextManipulatorTest extends Specification {
    def "execute valid command without replace"() {
        given:
        def textManipulator = new TextManipulator()
        def text = "Hello World"
        def command = "hhlhllhlhhll"

        when:
        def result = textManipulator.executeCommand(text, command)

        then:
        result.text == "Hello World"
        result.cursorPosition == 2
    }

    def "execute valid command with replace"() {
        given:
        def textManipulator = new TextManipulator()
        def text = "Hello World"
        def command = "rhllllllrw"

        when:
        def result = textManipulator.executeCommand(text, command)

        then:
        result.text == "hello world"
        result.cursorPosition == 6
    }

    def "execute valid command with multiplication 1"() {
        given:
        def textManipulator = new TextManipulator()
        def text = "Hello World"
        def command = "rh6l9l4hrw"

        when:
        def result = textManipulator.executeCommand(text, command)

        then:
        result.text == "hello world"
        result.cursorPosition == 6
    }

    def "execute valid command with multiplication 2"() {
        given:
        def textManipulator = new TextManipulator()
        def text = "Hello World"
        def command = "9lrL7h2rL"

        when:
        def result = textManipulator.executeCommand(text, command)

        then:
        result.text == "HeLLo WorLd"
        result.cursorPosition == 3
    }

    def "execute valid command with multiplication 3"() {
        given:
        def textManipulator = new TextManipulator()
        def text = "Hello World"
        def command = "999999999999999999999999999lr0"

        when:
        def result = textManipulator.executeCommand(text, command)

        then:
        result.text == "Hello Worl0"
        result.cursorPosition == 10
    }

    def "execute valid command with multiplication 4"() {
        given:
        def textManipulator = new TextManipulator()
        def text = "Hello World"
        def command = "999rs"

        when:
        def result = textManipulator.executeCommand(text, command)

        then:
        result.text == "sssssssssss"
        result.cursorPosition == 10
    }
}
