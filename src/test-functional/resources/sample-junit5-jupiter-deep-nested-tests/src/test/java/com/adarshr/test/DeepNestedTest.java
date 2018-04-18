package com.adarshr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

public class DeepNestedTest {

    @Nested
    public class NestedTestsetLevelOne {

        @Test
        public void nestedTestsetLevelOne() {
            Assertions.assertEquals(1, 1);
        }

        @Nested
        public class NestedTestsetLevelTwo {

            @Test
            public void nestedTestsetLevelTwo() {
                Assertions.assertEquals(1, 1);
            }
        }
    }
}
