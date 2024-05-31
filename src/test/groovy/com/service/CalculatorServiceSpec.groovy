package com.service

import spock.lang.Specification

class CalculatorServiceSpec extends Specification {

    def "addTen method checks for non-negative argument in getBaseValue"() {
        given:
        DependencyService dependencyService = Mock(DependencyService)
        CalculatorService calculatorService = new CalculatorService(dependencyService)

        when:
        def result = calculatorService.addTen(5)

        then:
        1 * dependencyService.getBaseValue({ it >= 0 }) >> 10
        result == 15
    }
}
