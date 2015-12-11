package com.jdriven.xmastree;

import java.util.Arrays;

import static java.lang.Math.*;

public class Averages {
    private static final int DEFAULT_LENGTH = 5;

    private int[] min;
    private int[] max;
    private int last;

    public Averages() {
        this(DEFAULT_LENGTH);
    }

    public Averages(int seconds) {
        this.min = new int[seconds];
        this.max = new int[seconds];

        Arrays.fill(min, -1);
        Arrays.fill(max, -1);
    }

    public void update(int level) {
        int current = (int)((System.currentTimeMillis() % min.length * 1000) / 1000);
        if(current != last) {
            last = current;
            min[current] = Integer.MAX_VALUE;
            max[current] = Integer.MIN_VALUE;
        }

        min[current] = min(min[current], level);
        max[current] = max(max[current], level);
    }

    public int averageMin() {
        return averageFor(min);
    }

    public int averageMax() {
        return averageFor(max);
    }

    private int averageFor(int[] array) {
        int c = 0;
        int s = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] >= 0) {
                c++;
                s += array[i];
            }
        }

        return s / c;
    }
}
