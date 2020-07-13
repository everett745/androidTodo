package com.example.todoapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;

public class Cutter extends AsyncTask<Uri, Void, Bitmap[]> {
    private int fps = 5;
    private int videoLength = 3;
    private final int second = 1_000_000;
    private Context context;


    public Cutter(Context context) {
        this.context = context;
    }

    public int getFps() {
        return fps;
    }

    public int getVideoLength() {
        return videoLength;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void setVideoLength(int videoLength) {
        this.videoLength = videoLength;
    }

    @Override
    protected Bitmap[] doInBackground(Uri... uris) {
        if (uris == null || uris[0] == null || uris.length == 0)
            return null;
        Bitmap[] frames = new Bitmap[fps * videoLength];
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uris[0]);
        for (int i = 0; i < fps * videoLength; i++) {
            int current_time = second * i / fps;
            frames[i] = retriever.getFrameAtTime(current_time, MediaMetadataRetriever.OPTION_CLOSEST);
        }
        return frames;
    }
}
