package com.jdriven.fadecandy.lib;

/**
 * Created by niels on 6-11-15.
 */
public class Pixel {
    public int r;
    public int g;
    public int b;

    public Pixel(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public void set(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
