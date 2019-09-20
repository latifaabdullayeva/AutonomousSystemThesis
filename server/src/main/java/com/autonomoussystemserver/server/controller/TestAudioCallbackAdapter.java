package com.autonomoussystemserver.server.controller;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.callback.DefaultAudioCallbackAdapter;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TestAudioCallbackAdapter extends DefaultAudioCallbackAdapter {

    /**
     * Output stream.
     */
    private final BufferedOutputStream out;

    /**
     * Create an audio callback.
     */
    public TestAudioCallbackAdapter(File output) throws IOException {
        super(4); // 4 is the block size for the audio samples
        out = new BufferedOutputStream(new FileOutputStream(output));
    }

    @Override
    protected void onPlay(MediaPlayer mediaPlayer, byte[] data, int sampleCount, long pts) {
        try {
            out.write(data);
        } catch (IOException e) {
            // Can't really do anything, should stop the media player I suppose...
            e.printStackTrace();
        }
    }

    @Override
    public void flush(MediaPlayer mediaPlayer, long pts) {
        System.out.println("flush()");
    }

    @Override
    public void drain(MediaPlayer mediaPlayer) {
        System.out.println("drain()");
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}