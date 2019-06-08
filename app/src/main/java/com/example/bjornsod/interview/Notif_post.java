package com.example.bjornsod.interview;

public class Notif_post {

    private String description;
    private String username;
    private int thumbnail_profile;

    public Notif_post(String description, String username, int thumbnail_profile) {
        this.description = description;
        this.username = username;
        this.thumbnail_profile = thumbnail_profile;
    }

    public String getDescription() {
        return description;
    }

    public String getUsername() {
        return username;
    }

    public int getThumbnail_profile() {
        return thumbnail_profile;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setThumbnail_profile(int thumbnail_profile) {
        this.thumbnail_profile = thumbnail_profile;
    }


}
