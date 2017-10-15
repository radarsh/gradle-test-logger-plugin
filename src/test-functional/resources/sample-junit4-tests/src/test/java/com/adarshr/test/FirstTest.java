package com.adarshr.test;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.runners.MethodSorters.NAME_ASCENDING;


@FixMethodOrder(NAME_ASCENDING)
public class FirstTest {

    @Test
    public void thisTestShouldPass() {
        Assert.assertEquals(1, 1);
    }

    @Test
    public void thisTestShouldFail() {
        Assert.assertEquals(1, 2);
    }

    @Test
    @Ignore
    public void thisTestShouldBeSkipped() {
        Assert.assertEquals(1, 1);
    }
}
