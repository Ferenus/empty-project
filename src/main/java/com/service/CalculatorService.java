package com.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculatorService {
    private static final Logger log = LoggerFactory.getLogger(CalculatorService.class);

    private final DependencyService dependencyService;

    public CalculatorService(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    public int addTen(int number) {
        log.info("number = {}", number);
        int baseValue = dependencyService.getBaseValue(0);
        return number + baseValue;
    }
}