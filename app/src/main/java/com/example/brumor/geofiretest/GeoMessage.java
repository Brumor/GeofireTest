package com.example.brumor.geofiretest;

/**
 * Created by brumor on 11/6/17.
 */

public class GeoMessage {
    String content;

    public GeoMessage() {};

    public GeoMessage(String content) {
        this.content = content;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
