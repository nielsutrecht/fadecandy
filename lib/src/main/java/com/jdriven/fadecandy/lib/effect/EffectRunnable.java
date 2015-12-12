package com.jdriven.fadecandy.lib.effect;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;
import com.jdriven.fadecandy.lib.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EffectRunnable implements Runnable {
    private List<Effect> effects;
    private Effect highPrio;
    private int index;
    private boolean continuous;
    private boolean started;

    private Server.Channel channel;

    public EffectRunnable(Server.Channel channel) {
        this(channel, false);
    }

    public EffectRunnable(Server.Channel channel, boolean continuous) {
        this.channel = channel;
        this.continuous = continuous;
        this.effects = new ArrayList<>();
        this.index = 0;

    }

    public EffectRunnable add(Effect effect) {
        effects.add(effect);

        return this;
    }

    public EffectRunnable setHighPrio(Effect highPrio) {
        this.highPrio = highPrio;

        return this;
    }

    public EffectRunnable add(Function<PixelInfo, Pixel> function, int interval, int duration) {
        BasicEffect effect = new BasicEffect(function);
        effect.setInterval(interval);
        effect.setDuration(duration);

        add(effect);

        return this;
    }

    @Override
    public void run() {
        started = true;
        if(effects.size() == 0) {
            return;
        }
        while(started) {
            long effectStart = System.currentTimeMillis();
            Effect effect = current();
            int effectDuration = effect.getDuration();
            while(effect.ready() && started && (System.currentTimeMillis() - effectStart) < effectDuration) {
                try {
                    if(highPrio != null && highPrio.ready()) {
                        channel.setPixels(highPrio.getFunction()).write();
                        System.out.println("Hi");
                    }
                    else {
                        channel.setPixels(effect.getFunction()).write();
                        System.out.println("Lo");
                    }
                }
                catch(IOException e) {
                    throw new RuntimeException(e);
                }
                sleep();
            }
            index++;
            if(index >= effects.size()) {
                if(continuous) {
                    index = 0;
                }
                else {
                    return;
                }
            }
        }
    }

    public void stop() {
        started = false;
    }

    private Effect current() {
        return effects.get(index);

    }


    private void sleep() {
        try {
            Thread.sleep(current().getInterval());
        }
        catch (InterruptedException e) {
        }
    }
}
