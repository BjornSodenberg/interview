package com.example.bjornsod.interview;

import java.sql.Timestamp;

public class Post {

    public String user_id;
    public String image_url;
    public String desc;
    public String title;
    public String image_thumb;



    public Post() {
    }

    public Post(String user_id, String image_url, String desc, String title, String image_thumb, Timestamp timestamp) {
        this.user_id = user_id;
        this.image_url = image_url;
        this.desc = desc;
        this.title = title;
        this.image_thumb = image_thumb;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }


    public String getUser_id() {
        return user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public String getImage_thumb() {
        return image_thumb;
    }

}
