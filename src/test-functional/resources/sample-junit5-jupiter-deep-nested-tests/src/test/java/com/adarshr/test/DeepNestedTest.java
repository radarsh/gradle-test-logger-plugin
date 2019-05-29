package com.adarshr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.DisplayName;

public class DeepNestedTest {

    @Nested
    public class NestedTestsetLevelOne {

        @Test
        public void nestedTestsetLevelOne() {
            Assertions.assertEquals(1, 1);
        }

        @Nested
        @DisplayName("Nested test set level two")
        public class NestedTestsetLevelTwo {

            @Test
            public void nestedTestsetLevelTwo() {
                Assertions.assertEquals(1, 1);
            }
        }
    }
}
