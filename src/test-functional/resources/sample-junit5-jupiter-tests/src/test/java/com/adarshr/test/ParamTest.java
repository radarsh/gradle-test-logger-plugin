package com.adarshr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Arrays;

public class ParamTest {

    @MethodSource("allParams")
    @DisplayName("Testing parameterized tests")
    @ParameterizedTest(name = "param {0} is not null")
    public void testThisParam(String param) {
        Assertions.assertNotNull(param);
    }

    private static List<String> allParams() {
        return Arrays.asList("One", "Two", "Three");
    }
}
