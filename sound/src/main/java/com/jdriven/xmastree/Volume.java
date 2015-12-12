package com.jdriven.xmastree;


import javax.sound.sampled.*;

public class Volume implements Runnable {

    private double current;
    private boolean cont;
    private Averages averages = new Averages(60);

    @Override
    public void run() {
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

            double min = Double.MAX_VALUE;
            double max = Double.MIN_VALUE;
            cont = true;
            while (cont) {
                numBytesRead = targetLine.read(targetData, 0, targetData.length);

                if (numBytesRead == -1)	break;

                double rms = calculateRMSLevel(targetData);


                min = Math.min(min, rms);
                max = Math.max(max, rms);

                double calculated = (rms - min);

                current = calculated / (max - min);
                if(max - min < 2.0) {
                    current = 0.0;
                }
                max -= 0.1;
                Thread.sleep(20);
            }
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public SoundValues get() {
        return new SoundValues(current);
    }

    public void stop() {
        cont = false;
    }

    protected static double calculateRMSLevel(byte[] audioData) { // audioData might be buffered data read from a data line
        long lSum = 0;
        for(int i=0; i<audioData.length; i++)
            lSum = lSum + audioData[i];

        double dAvg = lSum / audioData.length;

        double sumMeanSquare = 0d;
        for(int j=0; j<audioData.length; j++)
            sumMeanSquare = sumMeanSquare + Math.pow(audioData[j] - dAvg, 2d);

        double averageMeanSquare = sumMeanSquare / audioData.length;
        return (Math.pow(averageMeanSquare,0.5d) + 0.5);
    }

    public static class SoundValues {
        public final double current;

        public SoundValues(double current) {
            this.current = current;
        }
    }
}
