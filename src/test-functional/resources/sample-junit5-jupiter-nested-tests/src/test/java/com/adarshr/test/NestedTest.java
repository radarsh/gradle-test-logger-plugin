package com.adarshr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

public class NestedTest {

    @Nested
    public class NestedTestsetOne {

        @Test
        public void firstTestOfNestedTestsetOne() {
            Assertions.assertEquals(1, 1);
        }

        @Test
        public void secondTestOfNestedTestsetOne() {
            Assertions.assertEquals(1, 1);
        }
    }

    @Nested
    public class NestedTestsetTwo {

        @Test
        public void firstTestOfNestedTestsetTwo() {
            Assertions.assertEquals(1, 1);
        }

        @Test
        public void secondTestOfNestedTestsetTwo() {
            Assertions.assertEquals(1, 1);
        }
    }

    @Nested
    public class NestedTestsetThree {

        @Test
        public void firstTestOfNestedTestsetThree() {
            Assertions.assertEquals(1, 1);
        }
    }
}
