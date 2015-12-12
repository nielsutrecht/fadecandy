package com.jdriven.xmastree;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;
import com.jdriven.fadecandy.lib.effect.Effect;

import java.util.function.Function;

public class SoundEffect implements Effect {
    private final Volume volume = new Volume();
    private Function<PixelInfo, Pixel> function;

    public SoundEffect(Effect effect) {
        Thread vThread = new Thread(volume);
        vThread.setName("Volume thread");
        vThread.setDaemon(true);
        vThread.start();

        switch(effect) {
            case VU_METER:
                function = vuMeter();
                break;
        }
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
        return function;
    }

    @Override
    public boolean ready() {
        return volume.get().current > 0.0;
    }

    public enum Effect {
        VU_METER
    }

    private Function<PixelInfo, Pixel> vuMeter() {
        return (pi) -> {
            int pixels = (int)(pi.length * volume.get().current);

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
}
