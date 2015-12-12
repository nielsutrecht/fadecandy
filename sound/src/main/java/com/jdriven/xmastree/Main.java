package com.jdriven.xmastree;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;
import com.jdriven.fadecandy.lib.Server;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.function.Function;

public class Main {
    private Server server;
    private Volume volume = new Volume();

    public Main() throws IOException {
        server = new Server();
        server.connect();
        server.createChannel(0, 64);
        Thread vThread = new Thread(volume);
        vThread.setName("Volume reader");
        vThread.setDaemon(true);
        vThread.start();
    }
    public static void main(String... argv) throws Exception {
        Main main = new Main();
        main.testSound();
    }

    public void testSound() throws Exception {
        while(true) {
            double v = volume.get().current;
            setPixels(v);
            Thread.sleep(20);
        }
    }

    public void setPixels(double value) throws Exception {
        Function<PixelInfo, Pixel> vuMeter = (pi) -> {
            int pixels = (int)(pi.length * value);

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

        server.channel(0).setPixels(vuMeter).write();
    }
}
