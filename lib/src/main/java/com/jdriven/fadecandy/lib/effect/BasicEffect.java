package com.jdriven.fadecandy.lib.effect;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by niels on 6-11-15.
 */
public class BasicEffect implements Effect {
    private static Supplier<Integer> DEFAULT_DURATION = () -> 10000;
    private static Supplier<Integer> DEFAULT_INTERVAL = () -> 500;
    private Function<PixelInfo, Pixel> function;

    public BasicEffect(Function<PixelInfo, Pixel> function) {
        this.function = function;
    }

    private Supplier<Integer> duration = DEFAULT_DURATION;
    private Supplier<Integer> interval = DEFAULT_INTERVAL;

    public void setInterval(Supplier<Integer> interval) {
        this.interval = interval;
    }

    public void setInterval(int interval) {
        setInterval(() -> interval);
    }

    public void setDuration(Supplier<Integer> duration) {
        this.duration = duration;
    }

    public void setDuration(int duration) {
        setDuration(() -> duration);
    }

    @Override
    public int getInterval() {
        return interval.get();
    }

    @Override
    public int getDuration() {
        return duration.get();
    }

    @Override
    public Function<PixelInfo, Pixel> getFunction() {
        return function;
    }
}
