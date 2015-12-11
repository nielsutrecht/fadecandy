package com.jdriven.xmastree;


import javazoom.jl.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

public class PlaySongs {

    private static final Logger LOG = LoggerFactory.getLogger(PlaySongs.class);

    private void play() {
        LOG.info("Starting playing Chrismas songs :-)");
        List<File> songs = getSongs();

        while(true) {
            songs.forEach(song -> {
                try{
                    LOG.info("Start song '{}'", song.getName());
                    FileInputStream fis = new FileInputStream(song);
                    Player playMP3 = new Player(fis);
                    playMP3.play();
                }
                catch(Exception exc){
                    LOG.error("Failed to play the file. {}", exc);
                }
            });
        }
    }

    private List<File> getSongs() {
        File folder = new File("/home/pi/Music/christmas");
        File[] listOfFiles = folder.listFiles();
        return Arrays.asList(listOfFiles);
    }

    public static void main(String[] args) {
        PlaySongs playSongs = new PlaySongs();
        playSongs.play();
    }
}
