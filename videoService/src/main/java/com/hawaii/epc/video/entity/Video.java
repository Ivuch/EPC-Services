package com.hawaii.epc.video.entity;

import java.io.Serializable;
import java.util.UUID;

public class Video implements Serializable {
    private String id;
    private String name;
    private String path;

    public Video() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
    }
    public Video(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}