package com.adarshr.test;

import org.testng.Assert;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

public class SecondTest {

    @Test
    public void thisTestShouldPass() {
        Assert.assertEquals(1, 1);
    }

    @Test
    public void thisTestShouldFail() {
        Assert.assertEquals(1, 2);
    }

    @Ignore
    public void thisTestShouldBeSkipped() {
        Assert.assertEquals(1, 1);
    }
}
