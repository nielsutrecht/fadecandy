package com.jdriven.fadecandy.lib;

import org.junit.Test;

import java.awt.Color;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by niels on 6-11-15.
 */
public class ServerTest {
    private Random random = new Random();
    private Server server;

    private Function<PixelInfo, Pixel> randomFunc = (pi) -> {
        pi.pixel.r = random.nextInt(256);
        pi.pixel.g = random.nextInt(256);
        pi.pixel.b = random.nextInt(256);

        return pi.pixel;
    };

    private Function<PixelInfo, Pixel> sineFunc = (pi) -> {
        double time = (double)(System.currentTimeMillis() % 4000) / 4000.0 * Math.PI * 2.0;

        time = (Math.sin(time) + 1.0) / 2.0;

        pi.pixel.r = (int)(time * 256.0);
        pi.pixel.g = (int)(time * 256.0);
        pi.pixel.b = (int)(time * 256.0);

        return pi.pixel;
    };

    public ServerTest() throws Exception {
        server = new Server();
        server.connect();
        server.createChannel(0, 8);
    }

    @Test
    public void testSetPixelsWithColor() throws Exception {
        for(int i = 0;i < 1000;i++) {
            Thread.sleep(500);
            server.channel(0).setPixels(Color.RED).write();
            Thread.sleep(500);
            server.channel(0).setPixels(Color.GREEN).write();
            Thread.sleep(500);
            server.channel(0).setPixels(Color.BLUE).write();
        }
    }

    @Test
    public void testSetPixelsWithBiFunction() throws Exception {
        final Random random = new Random();
        for(int i = 0;i < 1000;i++) {
            server.channel(0).setPixels(sineFunc).write();

            Thread.sleep(500);
        }
    }


}