package com.jdriven.xmastree;

import com.jdriven.fadecandy.lib.Pixel;
import com.jdriven.fadecandy.lib.PixelInfo;
import com.jdriven.fadecandy.lib.Server;
import com.jdriven.fadecandy.lib.effect.EffectFunctions;
import com.jdriven.fadecandy.lib.effect.EffectRunnable;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {
    private static final Pattern EFFECT_PATTERN = Pattern.compile("(?<func>[a-zA-Z]+)\\((?<param>[0-9,]+)\\),(?<int>[0-9]+),(?<dur>[0-9]+)");
    private static final String DEFAULT_CONFIG = "/tree.properties";

    private Properties properties;
    private Config() {
        properties = new Properties();
    }

    public static Config from(InputStream ins) throws IOException {
        Config config = new Config();
        config.properties.load(ins);

        return config;
    }

    public static Config defaultConfig() {
        try {
            return from(Config.class.getResourceAsStream(DEFAULT_CONFIG));
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... argv) {
        Config.defaultConfig().getEffectRunnable(null, false);
    }

    public EffectRunnable getEffectRunnable(Server.Channel channel, boolean continuous) {
        EffectRunnable runnable = new EffectRunnable(channel, continuous);

       // List<String> keys = properties.keySet().stream().map(o -> (String)o).sorted((a, b) -> Integer.compare());

        for(Object o : properties.keySet()) {
            String key = (String)o;
            if(key.startsWith("effect.")) {
                addEffect(runnable, properties.getProperty(key));
            }
        }

        return runnable;
    }

    private void addEffect(EffectRunnable runnable, String effect) {
        Matcher m = EFFECT_PATTERN.matcher(effect);

        if(!m.matches()) {
            throw new IllegalArgumentException("Effect doesn't match regex: " + effect);
        }

        List<Integer> params = Stream.of(m.group("param").split(",")).map(Integer::parseInt).collect(Collectors.toList());
        int interval = Integer.parseInt(m.group("int"));
        int duration = Integer.parseInt(m.group("dur"));

        Function<PixelInfo, Pixel> func;
        switch(m.group("func")) {
            case "sine": func = EffectFunctions.sine(params.get(0),params.get(1),params.get(2),params.get(3));break;
            case "vuMeter": func = EffectFunctions.vuMeter(params.get(0));
            default: throw new IllegalArgumentException("Unknown function: " + m.group("func"));
        }

        runnable.add(func, interval, duration);
    }
}
