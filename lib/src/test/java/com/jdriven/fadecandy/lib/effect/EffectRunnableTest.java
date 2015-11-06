package com.jdriven.fadecandy.lib.effect;

import com.jdriven.fadecandy.lib.Server;
import org.junit.Test;

/**
 * Created by niels on 6-11-15.
 */
public class EffectRunnableTest {
    private Server server;
    public EffectRunnableTest() throws Exception {
        server = new Server();
        server.connect();
        server.createChannel(0, 8);
    }
    @Test
    public void testRunnable() throws Exception {
        EffectRunnable runnable = new EffectRunnable(server.channel(0), true);
        runnable
                .add(EffectFunctions.kitt(), 100, 10000)
                .add(EffectFunctions.hueSine(10000), 50, 10000)
                .add(EffectFunctions.random(), 200, 10000)
                .add(EffectFunctions.sine(4000), 500, 4000)
                .add(EffectFunctions.fire(), 500, 10000)
                .add(EffectFunctions.sine(4000, 0, 0, 256), 500, 4000)
                .add(EffectFunctions.vuMeter(4000), 100, 8000)
                .add(EffectFunctions.sine(4000, 256, 0, 0), 500, 8000);

        Thread.sleep(500);
        runnable.run();
    }
}