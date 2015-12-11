package com.jdriven.fadecandy.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by niels on 6-11-15.
 */
public class Server {
    public static final int DEFAULT_PORT = 7890;
    public static final String DEFAULT_HOSTNAME = "localhost";

    private static final int COMMAND_SET_PIXELS = 0;
    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private String hostname;
    private int port;
    private Socket socket;
    private OutputStream out;

    private Map<Integer, Channel> channels = new HashMap<>();

    public Server() {
        this(DEFAULT_HOSTNAME);
    }

    public Server(String hostname) {
        this(hostname, DEFAULT_PORT);
    }

    public Server(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws IOException {
        LOG.info("Connecting to {}:{}", hostname, port);
        socket = new Socket(hostname, port);
        socket.setTcpNoDelay(true);
        socket.setKeepAlive(true);
        out = socket.getOutputStream();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Thread reader = new Thread(() -> {
            try {
                while(true) {
                    LOG.info("Reply: {}", in.readLine());
                }
            }
            catch(Exception e) {
            }
        });

        reader.setDaemon(true);
        reader.start();

        sendFirmwareConfig();

        LOG.info("Connected");
    }

    private void writeData(byte[] bytes) throws IOException {
        out.write(bytes);
        out.flush();
    }

    public void test() throws IOException {
        sendFirmwareConfig();
    }

    private void sendFirmwareConfig() throws IOException {
        byte[] packet = new byte[9];
        packet[0] = (byte) 0; // Channel (reserved)
        packet[1] = (byte) 0xFF; // Command (System Exclusive)
        packet[2] = 0; // Length high byte
        packet[3] = 5; // Length low byte
        packet[4] = 0x00; // System ID high byte
        packet[5] = 0x01; // System ID low byte
        packet[6] = 0x00; // Command ID high byte
        packet[7] = 0x02; // Command ID low byte
        packet[8] = 0b00001000;

        writeData(packet);
    }

    public void createChannel(int channel, int pixels) {
        assertChannelNumber(channel);

        channels.put(channel, new Channel(channel, pixels));
    }

    public Channel channel(int channel) {
        assertChannelNumber(channel);

        return channels.get(channel);
    }

    private void assertChannelNumber(int channel) {
        if(channel > 7 || channel < 0) {
            throw new IllegalArgumentException("Only channels from 0 to 7 inclusive are valid");
        }
    }

    public class Channel {
        private int channel;
        private Pixel[] pixels;

        public Channel(int channel, int pixels) {
            this.channel = channel;
            this.pixels = new Pixel[pixels];

            for(int i = 0;i < this.pixels.length;i++) {
                this.pixels[i] = new Pixel(0 , 0, 0);
            }
        }

        public Channel setPixels(Pixel... pixels) throws IOException {
            if(pixels.length != this.pixels.length) {
                throw new IllegalArgumentException("Provided array length doesn't match current length " + this.pixels.length);
            }

            this.pixels = pixels;

            return this;
        }

        public Channel setPixels(int r, int g, int b) {
            for(Pixel p : pixels) {
                p.r = r;
                p.g = g;
                p.b = b;
            }

            return this;
        }

        public Channel setPixels(Pixel p) {
            setPixels(p.r, p.g, p.b);

            return this;
        }

        public Channel setPixels(Function<PixelInfo, Pixel> func) {
            for(int i = 0;i < pixels.length;i++) {
                pixels[i] = func.apply(new PixelInfo(pixels[i], i, pixels.length));
            }

            return this;
        }

        public Channel setPixels(Color c) {
            setPixels(c.getRed(), c.getGreen(), c.getBlue());

            return this;
        }

        public Channel setPixel(int index, Pixel pixel) {
            pixels[index] = pixel;

            return this;
        }

        public Channel setPixel(int index, Color color) {
            pixels[index].r = color.getRed();
            pixels[index].g = color.getGreen();
            pixels[index].b = color.getBlue();

            return this;
        }

        public Channel setPixel(int index, int r, int g, int b) {
            pixels[index].r = r;
            pixels[index].g = g;
            pixels[index].b = b;

            return this;
        }

        public Pixel getPixel(int index) {
            return pixels[index];
        }

        public void write() throws IOException {
            byte[] packet = new byte[4 + pixels.length * 3];

            int i = 0;
            packet[i++] = (byte)(channel & 0xFF);
            packet[i++] = (byte)COMMAND_SET_PIXELS;
            packet[i++] = 0;
            packet[i++] = (byte)(pixels.length * 3);

            for(Pixel p : pixels) {
                packet[i++] = (byte)p.r;
                packet[i++] = (byte)p.g;
                packet[i++] = (byte)p.b;
            }

            LOG.debug("Sending {} pixels to channel {}", pixels.length, channel);
            writeData(packet);
        }
    }

}
