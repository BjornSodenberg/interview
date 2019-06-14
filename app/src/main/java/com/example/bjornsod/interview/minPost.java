package com.example.bjornsod.interview;

public class minPost {

    public String nameMinPost;
    public String titleMinPost;
    public String descMinPost;
    public String imageMinPost;

    public minPost(String nameMinPost, String titleMinPost, String descMinPost, String imageMinPost) {
        this.nameMinPost = nameMinPost;
        this.titleMinPost = titleMinPost;
        this.descMinPost = descMinPost;
        this.imageMinPost = imageMinPost;
    }

    public minPost() {
    }

    public String getNameMinPost() {
        return nameMinPost;
    }

    public void setNameMinPost(String nameMinPost) {
        this.nameMinPost = nameMinPost;
    }

    public String getTitleMinPost() {
        return titleMinPost;
    }

    public void setTitleMinPost(String titleMinPost) {
        this.titleMinPost = titleMinPost;
    }

    public String getDescMinPost() {
        return descMinPost;
    }

    public void setDescMinPost(String descMinPost) {
        this.descMinPost = descMinPost;
    }

    public String getImageMinPost() {
        return imageMinPost;
    }

    public void setImageMinPost(String imageMinPost) {
        this.imageMinPost = imageMinPost;
    }
}
