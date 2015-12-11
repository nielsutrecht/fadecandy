package com.jdriven.xmastree;

import org.junit.Test;

import java.util.Random;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class AveragesTest {
    @Test
    public void testAverages() {
        Averages avg = new Averages();
        Random random = new Random();
        long start = System.currentTimeMillis();

        while(System.currentTimeMillis() - start < 5500) {
            avg.update(random.nextInt(1000) + 1000);

            assertThat(avg.averageMin() <= avg.averageMax(), equalTo(true));
            assertThat(avg.averageMin() >= 1000, equalTo(true));
            assertThat(avg.averageMax() <= 1000, equalTo(true));
        }
    }
}