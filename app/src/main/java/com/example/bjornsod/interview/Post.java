package com.example.bjornsod.interview;

public class Post {

    private String title;
    private String category;
    private String description;
    private int thumbnail;

    private String username;
    private int thumbnail_profile;




    public Post() {
    }

    public Post(String title, String category, String description, int thumbnail, String username, int thumbnail_profile) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.thumbnail = thumbnail;
        this.username = username;
        this.thumbnail_profile = thumbnail_profile;
    }

    public Post(String title, String category, String description, int thumbnail) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public String getUsername() { return username; }

    public int getThumbnail_profile() { return thumbnail_profile; }


    public void setTitle(String title) { this.title = title; }

    public void setCategory(String category) { this.category = category; }

    public void setDescription(String description) { this.description = description; }

    public void setThumbnail(int thumbnail) { this.thumbnail = thumbnail; }

    public void setUsername(String username) { this.username = username; }

    public void setThumbnail_profile(int thumbnail_profile) { this.thumbnail_profile = thumbnail_profile; }


}
