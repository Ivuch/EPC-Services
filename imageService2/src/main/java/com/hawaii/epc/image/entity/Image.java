package com.hawaii.epc.image.entity;

import java.io.Serializable;
import java.util.UUID;

public class Image implements Serializable {

    private String id;
    private String itemImageSrc;
    private String thumbnailImageSrc;
    private String alt;
    private String title;

    public Image() {
        this.id = UUID.randomUUID().toString().substring(0, 8);
    }

    public Image(String itemImageSrc, String thumbnailImageSrc, String alt, String title) {
        this();
        this.itemImageSrc = itemImageSrc;
        this.thumbnailImageSrc = thumbnailImageSrc;
        this.alt = alt;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemImageSrc() {
        return itemImageSrc;
    }

    public void setItemImageSrc(String itemImageSrc) {
        this.itemImageSrc = itemImageSrc;
    }

    public String getThumbnailImageSrc() {
        return thumbnailImageSrc;
    }

    public void setThumbnailImageSrc(String thumbnailImageSrc) {
        this.thumbnailImageSrc = thumbnailImageSrc;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}