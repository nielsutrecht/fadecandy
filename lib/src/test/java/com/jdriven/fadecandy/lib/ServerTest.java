package com.jdriven.fadecandy.lib;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by niels on 6-11-15.
 */
public class ServerTest {
    private Server server;

    public ServerTest() throws Exception {
        server = new Server();
        server.connect();
    }

    @Test
    public void testSetPixelsRed() throws Exception {
        Pixel red = new Pixel(100, 0, 0);

        Pixel[] pixels = new Pixel[2];

        Arrays.fill(pixels, red);

        for(int i = 0;i < 1000;i++) {
            Thread.sleep(1000);
            server.channel(0).setPixels(pixels);
        }



    }
}