package com.jdriven.fadecandy.lib;

import org.junit.Test;

import javax.sound.sampled.*;
import java.awt.Color;
import java.util.Random;
import java.util.function.Function;

/**
 * Created by niels on 6-11-15.
 */
public class ServerTest {
    private Random random = new Random();
    private Server server;

    public ServerTest() throws Exception {
        server = new Server();
        server.connect();
        server.createChannel(0, 8);
    }


    public void testSound() throws Exception {
        AudioFormat format = new AudioFormat(44100, 16, 2, true, true);

        DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
        DataLine.Info sourceInfo = new DataLine.Info(SourceDataLine.class, format);

        try {
            TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
            targetLine.open(format);
            targetLine.start();

            SourceDataLine sourceLine = (SourceDataLine) AudioSystem.getLine(sourceInfo);
            sourceLine.open(format);
            sourceLine.start();

            int numBytesRead;
            byte[] targetData = new byte[targetLine.getBufferSize() / 5];

            int minRms = Integer.MAX_VALUE;
            int maxRms = Integer.MIN_VALUE;
            while (true) {
                numBytesRead = targetLine.read(targetData, 0, targetData.length);

                if (numBytesRead == -1)	break;

                int rms = calculateRMSLevel(targetData);
                minRms = Math.min(minRms, rms);
                maxRms = Math.max(maxRms, rms);

                double calculated = (rms - minRms);

                setPixels(calculated / 30.0);
            }
        }
        catch (Exception e) {
            System.err.println(e);
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

    protected static int calculateRMSLevel(byte[] audioData) { // audioData might be buffered data read from a data line
        long lSum = 0;
        for(int i=0; i<audioData.length; i++)
            lSum = lSum + audioData[i];

        double dAvg = lSum / audioData.length;

        double sumMeanSquare = 0d;
        for(int j=0; j<audioData.length; j++)
            sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;
        return (int)(Math.pow(averageMeanSquare,0.5d) + 0.5);
    }


}