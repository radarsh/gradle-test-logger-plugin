package com.adarshr.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class SecondTest {

    @Test
    public void thisTestShouldPass() {
        Assertions.assertEquals(1, 1);
    }

    @Test
    public void thisTestShouldFail() {
        Assertions.assertEquals(1, 2);
    }

    @Test
    @Disabled
    public void thisTestShouldBeSkipped() {
        Assertions.assertEquals(1, 1);
    }
}
