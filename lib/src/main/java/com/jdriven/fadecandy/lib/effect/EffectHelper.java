package com.jdriven.fadecandy.lib.effect;

import com.jdriven.fadecandy.lib.Pixel;

import java.awt.Color;
import java.util.Random;

/**
 * Created by niels on 6-11-15.
 */
public class EffectHelper {
    private static final Random RANDOM = new Random();

    public static int randomColor() {
        return RANDOM.nextInt(256);
    }

    public static int randomColor(int from, int to) {
        assertColor(from);
        assertColor(to);

        if(from > to) {
            throw new IllegalArgumentException("From can't be larger than to");
        }
        if(from == to) {
            return from;
        }
        else {
            return RANDOM.nextInt(to - from + 1) + from;
        }
    }

    public static void setRandom(Pixel pixel) {
        pixel.r = randomColor();
        pixel.g = randomColor();
        pixel.b = randomColor();
    }

    public static void setToHsb(Pixel pixel, float hue, float saturation, float brightness) {
        int rgb = Color.HSBtoRGB(hue, saturation, brightness);

        pixel.r = (rgb & 0xFF0000) >> 16;
        pixel.g = (rgb & 0xFF00) >> 8;
        pixel.b = (rgb & 0xFF);
    }

    public static void setToHue(Pixel pixel, float hue) {
        setToHsb(pixel, hue, 1.0f, 1.0f);
    }

    public static void assertColor(int color) {
        if(color < 0 || color > 255) {
            throw new IllegalArgumentException("Color outside of range 0 - 255");
        }
    }
}
