package com.jdriven.xmastree;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;
import com.jdriven.fadecandy.lib.effect.Effect;

import java.util.function.Function;

public class SoundEffect implements Effect {
    public SoundEffect() {

    }
    @Override
    public int getInterval() {
        return 1;
    }

    @Override
    public int getDuration() {
        return 30000;
    }

    @Override
    public Function<PixelInfo, Pixel> getFunction() {
        return null;
    }

    @Override
    public boolean ready() {
        return false;
    }
}
