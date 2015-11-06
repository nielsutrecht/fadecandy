package com.jdriven.fadecandy.lib.effect;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static com.jdriven.fadecandy.lib.effect.EffectHelper.setRandom;
import static com.jdriven.fadecandy.lib.effect.EffectHelper.setToHue;

/**
 * Created by niels on 6-11-15.
 */
public class EffectFunctions {
    private EffectFunctions() {

    }

    public static Function<PixelInfo, Pixel> random() {
        final Random random = new Random();
        return (pi) -> {
            setRandom(pi.pixel);

            return pi.pixel;
        };
    }

    public static Function<PixelInfo, Pixel> fire() {
        final Random random = new Random();
        return (pi) -> {
            pi.pixel.r = 156 + random.nextInt(100);
            pi.pixel.g = random.nextInt(pi.pixel.r - 50);
            pi.pixel.b = 0;

            return pi.pixel;
        };
    }

    public static Function<PixelInfo, Pixel> sine(int duration) {
        return sine(duration, 256, 256, 256);
    }

    public static Function<PixelInfo, Pixel> sine(int duration, int r, int g, int b) {
        return (pi) -> {
            double time = (double)(System.currentTimeMillis() % duration) / (double)duration * Math.PI * 2.0;

            time = (Math.sin(time) + 1.0) / 2.0;

            pi.pixel.r = (int)(time * r);
            pi.pixel.g = (int)(time * g);
            pi.pixel.b = (int)(time * b);

            return pi.pixel;
        };
    }

    public static Function<PixelInfo, Pixel> hueSine(int duration) {
        return (pi) -> {
            double time = (double)(System.currentTimeMillis() % duration) / (double)duration * Math.PI * 2.0;

            time = (Math.sin(time) + 1.0) / 2.0;

            setToHue(pi.pixel, (float)time);

            return pi.pixel;
        };
    }

    public static Function<PixelInfo, Pixel> police(int duration) {
        return (pi) -> {
            int time = (int)(System.currentTimeMillis() % duration);
            Pixel[] pixels = new Pixel[2];
            if(time < duration / 2) {
                pixels[0] = Pixel.BLUE;
                pixels[1] = Pixel.RED;
            }
            else {
                pixels[0] = Pixel.RED;
                pixels[1] = Pixel.BLUE;
            }

            pi.pixel.set(pixels[pi.index % 2]);

            return pi.pixel;
        };
    }

    public static Function<PixelInfo, Pixel> vuMeter(int duration) {
        return (pi) -> {
            double time = (double)(System.currentTimeMillis() % duration) / (double)duration * Math.PI * 2.0;

            time = (Math.sin(time) + 1.0) / 2.0;

            int pixels = (int)(pi.length * time);

            pi.pixel.r = 0;
            pi.pixel.g = 0;
            pi.pixel.b = 0;

            if(pi.index <= pixels) {
                if(pi.index >= (int)(pi.length * 0.8)) {
                    pi.pixel.r = 255;
                }
                else {
                    pi.pixel.g = 255;
                }

            }

            return pi.pixel;
        };
    }

    public static Function<PixelInfo, Pixel> kitt() {
        final AtomicInteger index = new AtomicInteger(0);
        final AtomicBoolean direction = new AtomicBoolean(true);
        final AtomicInteger counter = new AtomicInteger(0);

        return (pi) -> {
            if(counter.get() >= pi.length) {
                if(direction.get()) {
                    index.incrementAndGet();
                }
                else {
                    index.decrementAndGet();
                }
                counter.set(0);
                if(index.get() >= pi.length || index.get() < 0) {
                    direction.set(!direction.get());
                }
            }

            pi.pixel.r = 0;
            pi.pixel.g = 0;
            pi.pixel.b = 0;

            int distance = Math.abs(pi.index - index.get());

            if(distance == 0) {
                pi.pixel.r = 255;
            }
            else if(distance == 1) {
                pi.pixel.r = 168;
            }
            else if(distance == 2) {
                pi.pixel.r = 84;
            }

            counter.incrementAndGet();

            return pi.pixel;
        };
    }
}
