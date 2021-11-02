package com.adarshr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;

public class DeepNestedTest {


    @BeforeAll
    public static void beforeAllDeepNestedTest() {
        System.out.println("DeepNestedTest.beforeAllDeepNestedTest");
    }

    @AfterAll
    public static void afterAllDeepNestedTest() {
        System.out.println("DeepNestedTest.afterAllDeepNestedTest");
    }

    @Nested
    public class NestedTestsetLevelOne {

        @Test
        public void nestedTestsetLevelOne() {
            System.out.println("NestedTestsetLevelOne.nestedTestsetLevelOne");
            Assertions.assertEquals(1, 1);
        }

        @Nested
        @DisplayName("Nested test set level two")
        public class NestedTestsetLevelTwo {

            @Test
            public void nestedTestsetLevelTwo() {
                System.out.println("NestedTestsetLevelTwo.nestedTestsetLevelTwo");
                Assertions.assertEquals(1, 1);
            }
        }
    }
}
