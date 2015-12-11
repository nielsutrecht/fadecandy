package com.jdriven.xmastree;

import com.jdriven.fadecandy.lib.Server;
import com.jdriven.fadecandy.lib.effect.EffectFunctions;
import com.jdriven.fadecandy.lib.effect.EffectRunnable;

import java.io.IOException;

public class Main {
    private Server server;
    private EffectRunnable runnable;

    public Main() throws IOException {
        server = new Server();
        server.connect();
        server.createChannel(0, 8);
    }

    public void setup() {
        runnable = new EffectRunnable(server.channel(0), true);
        runnable
                .add(EffectFunctions.police(800), 400, 4000)
                .add(EffectFunctions.kitt(), 100, 10000)
                .add(EffectFunctions.hueSine(10000), 50, 10000)
                .add(EffectFunctions.random(), 200, 10000)
                .add(EffectFunctions.sine(4000), 500, 4000)
                .add(EffectFunctions.fire(), 500, 10000)
                .add(EffectFunctions.sine(4000, 0, 0, 256), 500, 4000)
                .add(EffectFunctions.vuMeter(4000), 100, 8000)
                .add(EffectFunctions.sine(4000, 256, 0, 0), 500, 8000);
    }

    public void run() {
        runnable.run();
    }

    public static void main(String... argv) throws Exception {
        Main main = new Main();
        main.setup();
        main.run();
    }
}
