package com.jdriven.fadecandy.lib;

/**
 * Created by niels on 6-11-15.
 */
public class Pixel {
    public static final Pixel RED = new Pixel(255, 0, 0);
    public static final Pixel GREEN = new Pixel(0, 255, 0);
    public static final Pixel BLUE = new Pixel(0, 0, 255);

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

    public void set(Pixel p) {
        this.r = p.r;
        this.g = p.g;
        this.b = p.b;

    }
}
