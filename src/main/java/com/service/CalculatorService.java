package com.service;

public class CalculatorService {
    private final DependencyService dependencyService;

    public CalculatorService(DependencyService dependencyService) {
        this.dependencyService = dependencyService;
    }

    public int addTen(int number) {
        int baseValue = dependencyService.getBaseValue(0);
        return number + baseValue;
    }
}