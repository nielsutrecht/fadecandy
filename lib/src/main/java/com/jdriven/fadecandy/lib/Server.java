package com.jdriven.fadecandy.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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

        LOG.info("Connected");
    }

    private void writeData(byte[] bytes) throws IOException {
        out.write(bytes);
    }

    public void test() {
        byte[] bytes = bytes[]
    }

    private byte[] firmwareConfig() {
        byte[] firmware = new byte[10];
        firmware[0] = 0; //Channel
        firmware[1] = (byte)0xFF;
        firmware[2] = 0;
    }

    public Channel channel(int channel) {
        if(channel > 7 || channel < 0) {
            throw new IllegalArgumentException("Only channels from 0 to 7 inclusive are valid");
        }
        if(!channels.containsKey(channel)) {
            channels.put(channel, new Channel(channel));
        }

        return channels.get(channel);
    }

    public class Channel {
        private int channel;

        public Channel(int channel) {
            this.channel = channel;
        }

        public Channel setPixels(Pixel... pixels) throws IOException {
            LOG.info("Sending {} pixels to channel {}", pixels.length, channel);
            out.writeByte(channel);
            out.writeByte(COMMAND_SET_PIXELS);
            out.writeShort(pixels.length * 3);

            for(Pixel p : pixels) {
                out.writeByte(p.r);
                out.writeByte(p.g);
                out.writeByte(p.b);
            }

            out.flush();

            return this;
        }
    }

}
