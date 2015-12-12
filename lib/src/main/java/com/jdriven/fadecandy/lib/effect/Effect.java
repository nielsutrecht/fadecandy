package com.jdriven.fadecandy.lib.effect;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;

import java.util.function.Function;

public interface Effect {
    int getInterval();
    int getDuration();
    Function<PixelInfo, Pixel> getFunction();

    default boolean ready() {
        return true;
    }
}
